This is a set of classes for decrypting databases created by the "Remember Passwords" android app
published by Maryna Biletska, but apparently written by Eugene Beletskiy.

I used this app a long time ago, and even had a backed up passwords file e-mailed to my gmail account.
Eventually I forgot the password, I just remember that it was short because I had to type it every time
in order to use the app.
So I decided to figure out, as a saturday exercise, just how secure it is. 
Turns out, not very. It's very naive. I guess I always suspected that.

The password database is a json file containing an array of database entries:
    {"list":[
    {"id":1,"mLogin":"***","mNotes":"***","mPassword":"***","mStar":0,"mTitle":"***"}
    ,{"id":2,"mLogin":"***","mNotes":"***","mPassword":"***","mStar":0,"mTitle":"***"}
    ...
    ]}

Here, *** is a base-64 string representing the result of encoding. The cipher is PBEWithMD5AndDES,
using MD5 of user passsword as the key. Same string maps the the same ciphertext. "Secret" salt 
0x7d60435f02e9e0ae is used. Both this and the encryption logic are easy to obtain with JaDX 0.61 decompiler.

## Building And Running
In this directory are several classes:

Build all:
    `make`

brute force iteration to find a password that plausibly decrypts given string
    `java Guess "<base64 string>"`

decrypt a Remember Passwords database and print resulting Json to stdout
    `java DecryptJson <password> <path>`

decrypt a single string
    `java DecryptString <password> <encrypted string>`

I guess a standard disclaimer is in order -- I don't condone hacking of other people in any way.
Use this on your own data only.
