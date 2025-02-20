import cv2
import os

def encrypt_image(image_path, secret_message, password):
    img = cv2.imread(image_path)  # Read the image

    if img is None:
        print("Error: Unable to load image. Check the path.")
        return

    # Create character-to-value and value-to-character dictionaries
    d = {chr(i): i for i in range(255)}
    c = {i: chr(i) for i in range(255)}

    secret_message += "###"  # End marker to detect message end during decryption

    n, m, z = 0, 0, 0
    for i in range(len(secret_message)):
        img[n, m, z] = d[secret_message[i]]
        n += 1
        m += 1
        z = (z + 1) % 3

    encrypted_image_path = "encryptedImage.png"
    cv2.imwrite(encrypted_image_path, img)
    print(f"Message successfully encoded in {encrypted_image_path}")

    # Open the image (Windows only)
    os.system(f"start {encrypted_image_path}")  

def decrypt_image(image_path, password):
    img = cv2.imread(image_path)

    if img is None:
        print("Error: Unable to load image. Check the path.")
        return

    c = {i: chr(i) for i in range(255)}  # Reverse mapping

    entered_password = input("Enter passcode for decryption: ")
    if password != entered_password:
        print("Authentication failed!")
        return

    decrypted_message = ""
    n, m, z = 0, 0, 0

    while True:
        char = c[img[n, m, z]]
        if char == "#":  # Stop reading if end marker is found
            break
        decrypted_message += char
        n += 1
        m += 1
        z = (z + 1) % 3

    print("Decrypted Message:", decrypted_message)

# --- Usage ---
image_path = "mypic.png"  # Replace with the correct path
secret_message = input("Enter secret message: ")
password = input("Set a passcode: ")

encrypt_image(image_path, secret_message, password)
decrypt_image("encryptedImage.png", password)
