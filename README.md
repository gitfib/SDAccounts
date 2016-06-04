# SDAccounts
SDAccounts is an Xposed module for retaining user accounts on phone if their associated app isn't found during boot, as the case when the app is stored on an external storage that has yet to be mounted.

Currently supports Kitkat, Lollipop and Marshmallow.

After the wanted SD-stored app is ready, it might take few minutes for the account to load, and it might appear as if SDAccounts doesn't work.

If you performed a dalvik/art cache wipe, it could take a very long time for the apps and accounts to load.

If you try to sign in anyway, and receive an unusual error, that's possibly because the account has already been loaded!

Restart the app and check.
