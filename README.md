***

# RSA Encryption and Decryption

***

## Getting started

### Prerequisites

In order to run this program, you will need to have Java 8 or later installed on your computer
****

## Installing

To install the program, simply download the source code and compile it using your Java compiler.
****

## Usage

To use the program, simply run the `RSApplication` class. This will launch the GUI interface. But for the simplicity, I coded the program using long types instead of a BigInteger.
While running the program I would like to state that the program language is in dutch as well as the instructions in the application. 
You can also switch the instructions in the program using the switch instruction button.

****

## Encryption

The encryption allows you to encrypt a message using RSA encryption. To do so, follow these steps:

- Type a message that you want to encrypt.
- Enter a (secret) number in the text field for N.
- Generate a p and q by pressing the button. (The p and q are derived from N). You will also see how long it took to calculate p and q.
- Now that P and q are known, you can also generate E. Press the button to generate a random E that is coprime.
- In the text area, you will see the encrypted message in the format -> c1, c2, c3, ….

****

## Decryption 

The decryption allows you to decrypt an encrypted message using RSA encryption. To do so, follow these steps:

- Type the encrypted message in the text field.
- Generate a D. (D can only be generated if N and p and q and E are known).
- Press on decipher message to decrypt the encrypted message.
- Read the sent message and share this with no one!
- In the program, you see the explanation again, with the switch instructions button you can read the instructions again from the Encryption and Decryption.

***

# Authors
This program was created by Lloyd.
***
