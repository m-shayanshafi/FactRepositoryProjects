/*
 * Main.java
 *
 * Created on May 18, 2007, 12:16 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

#include <shlobj.h>
#include <windows.h>
#include <wincrypt.h>
#include "FirstLaunch.h"
#include "BattleMechanics.h"
#include <malloc.h>

BOOL APIENTRY DllMain (HINSTANCE hInst     /* Library instance handle. */ ,
                       DWORD reason        /* Reason this function is being called. */ ,
                       LPVOID reserved     /* Not used. */ )
{
    /* Returns TRUE on success, FALSE on failure */
    return TRUE;
}

/**
 * Return the path to the Application Data directory on Windows.
 */
JNIEXPORT
jstring
JNICALL
Java_shoddybattleclient_FirstLaunch_getApplicationDataDirectory(JNIEnv *env, jclass) {
    TCHAR szPath[MAX_PATH];
    SHGetFolderPath(NULL, 0x1C, NULL, 0, szPath);
    return env->NewStringUTF(szPath);
}

/**
 * Get random bytes using the Crypto API on Windows.
 */
JNIEXPORT
jbyteArray
JNICALL
Java_mechanics_BattleMechanics_getRandomBytes(JNIEnv *env, jclass, jint num) {
    HCRYPTPROV hCryptProv;
    if (!CryptAcquireContext(&hCryptProv, NULL, NULL, PROV_RSA_FULL, 0)) {
        return NULL;
    }
    BYTE *ret = (BYTE *)alloca(sizeof(BYTE) * num);
    if (!CryptGenRandom(hCryptProv, num, ret)) {
        return NULL;
    }
    jbyteArray arr = env->NewByteArray(num);
    env->SetByteArrayRegion(arr, 0, num, (jbyte *)ret);
    return arr;
}
