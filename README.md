# SDAccounts
Retains user accounts on phone if their associated app isn't found during boot, as the case when the app is stored on an external storage that has yet to be mounted.
Currently supports Kitkat, Lollipop and Marshmallow.

After the wanted app is ready, it might take few minutes for the account to load, and it might appear as if SDAccounts doesn't work.

If you performed a dalvik/art cache wipe, it could take a very long time for the apps and accounts to load.

If you try to sign in anyway, and receive an unusual error, that's possibly because the account has already been loaded! Restart the app and check.

Material Icons: Copyright © Google, licensed CC-BY 4.0 https://design.google.com/icons

App icon is based on Material Icons.

SDAccounts is licensed under zlib license:

Copyright © 2016 Amir Maimon, and contributors

This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.

Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgement in the product documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
