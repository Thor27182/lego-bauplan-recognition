<<<<<<< HEAD
#@author Franziska Budde
=======
#@author    Franziska Budde, Oliver Neumann, Lennart Brinkmann
>>>>>>> präsentation überarbeitet + nn datei

import tensorflow as tf
from tensorflow import keras
import numpy as np #array operation
import matplotlib.pyplot as plt #display stuff
import os
import cv2 #image operations

<<<<<<< HEAD
=======
gpus = tf.config.experimental.list_physical_devices('GPU')
if gpus:
    try:
        for gpu in gpus:
            tf.config.experimental.set_memory_growth(gpu, True)
    except RuntimeError as e:
        print(e)


>>>>>>> präsentation überarbeitet + nn datei


CATEGORIES = ["Hohlprofile", "Offene Profile", "Vollprofile"]
new_model = tf.keras.models.load_model('my_model(47loss51acc).h5')



IMG_SIZE = 100

<<<<<<< HEAD
img = plt.imread('Offen_1_0000.jpg')

=======
>>>>>>> präsentation überarbeitet + nn datei
img_array = cv2.imread('Offen_1_0000.jpg', cv2.IMREAD_GRAYSCALE)  # convert to array
new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))  # resize to normalize data
edges = cv2.Canny(new_array,100,200)

<<<<<<< HEAD
plt.imshow(edges, cmap='gray')
plt.show()
=======
>>>>>>> präsentation überarbeitet + nn datei

img = np.array(edges).reshape(-1, IMG_SIZE, IMG_SIZE, 1)


x=np.argmax(new_model.predict(img), axis=1)
v=CATEGORIES[x[0]]


<<<<<<< HEAD
print(v)

=======
keras.backend.clear_session()
print(v)
exit(1)
>>>>>>> präsentation überarbeitet + nn datei



