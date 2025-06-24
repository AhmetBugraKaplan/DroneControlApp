from flask import Flask, request
from threading import Thread
from pymavlink import mavutil
import time

app = Flask(__name__)

roll = 0.0      
throttle = 0.0  
pitch = 0.0     
mode_command = None
arm_command = None
master = None

def pwm_from_slider_roll(x):
    return int(1500 + (x * 500))

def pwm_from_slider_throttle(y):
    return int(1050 + (y * 1000))

def pwm_from_slider_pitch(z):
    return int(1500 + (z * 500))

@app.route('/update', methods=['POST'])
def handle_update():
    global roll, throttle, pitch
    data = request.get_json()
    roll = data.get('roll', 0.0)
    throttle = data.get('throttle', 0.0)
    pitch = data.get('pitch', 0.0)
    print(f"[LOG] Roll: {roll} -> {pwm_from_slider_roll(roll)} | Throttle: {throttle} -> {pwm_from_slider_throttle(throttle)} | Pitch: {pitch} -> {pwm_from_slider_pitch(pitch)}")
    return {"status": "ok"}

@app.route('/command', methods=['POST'])
def handle_command():
    global mode_command, arm_command
    data = request.get_json()
    # Mode komutu
    if "mode" in data:
        val = data["mode"]
        if val in ["loiter", "stabilize", "land"]:
            mode_command = val
            print(f"[LOG] MODE komutu: {val}")
        elif val in ["arm", "disarm"]:
            arm_command = val
            print(f"[LOG] ARM komutu: {val}")
    return {"status": "ok"}

def pixhawk_main():
    global master, mode_command, roll, throttle, pitch, arm_command
    print("[MAIN] Pixhawk bağlantısı kuruluyor...")
    master = mavutil.mavlink_connection('udp:0.0.0.0:14550')
    master.wait_heartbeat()
    print("[MAIN] Bağlantı kuruldu (heartbeat alındı).")

    while True:
        # Arm/disarm işlemleri
        if arm_command:
            if arm_command == "arm":
                print("[KOMUT] ARM komutu gönderildi.")
                master.arducopter_arm()
                master.motors_armed_wait()
                print("[KOMUT] ARM TAMAMLANDI.")
            elif arm_command == "disarm":
                print("[KOMUT] DISARM komutu gönderildi.")
                master.arducopter_disarm()
                master.motors_disarmed_wait()
                print("[KOMUT] DISARM TAMAMLANDI.")
            arm_command = None

        # Mod değiştirme komutu
        if mode_command:
            mode_id = None
            if mode_command.lower() == "loiter":
                mode_id = master.mode_mapping().get('LOITER')
            elif mode_command.lower() == "stabilize":
                mode_id = master.mode_mapping().get('STABILIZE')
            elif mode_command.lower() == "land":
                mode_id = master.mode_mapping().get('LAND')
            if mode_id is not None:
                print(f"[MODE] {mode_command.upper()} komutu gönderiliyor.")
                master.set_mode(mode_id)
            else:
                print("[MODE] Bilinmeyen mod komutu!")
            mode_command = None

        roll_pwm = pwm_from_slider_roll(roll)
        throttle_pwm = pwm_from_slider_throttle(throttle)
        pitch_pwm = pwm_from_slider_pitch(pitch)

        master.mav.rc_channels_override_send(
            master.target_system,
            master.target_component,
            roll_pwm,      # CH1: Roll
            pitch_pwm,     # CH2: Pitch
            throttle_pwm,  # CH3: Throttle
            65535, 65535, 65535, 65535, 65535, 65535
        )
        time.sleep(0.1)

def run_flask():
    print("[FLASK] Sunucu başlatılıyor...")
    app.run(host="0.0.0.0", port=5000, debug=False)

if __name__ == "__main__":
    flask_thread = Thread(target=run_flask)
    flask_thread.daemon = True
    flask_thread.start()
    pixhawk_main()
