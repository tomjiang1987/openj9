; This file designed to be assembled by the jasmin tool
; Jasmin can be obtained from http://jasmin.sourceforge.net/

;  Copyright (c) 2016, 2017 IBM Corp. and others
;  This program and the accompanying materials are made available under
;  the terms of the Eclipse Public License 2.0 which accompanies this
;  distribution and is available at https://www.eclipse.org/legal/epl-2.0/
;  or the Apache License, Version 2.0 which accompanies this distribution and
;  is available at https://www.apache.org/licenses/LICENSE-2.0.

;  This Source Code may also be made available under the following
;  Secondary Licenses when the conditions for such availability set
;  forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
;  General Public License, version 2 with the GNU Classpath
;  Exception [1] and GNU General Public License, version 2 with the
;  OpenJDK Assembly Exception [2].
;
;  [1] https://www.gnu.org/software/classpath/license.html
;  [2] http://openjdk.java.net/legal/assembly-exception.html
;
;  SPDX-License-Identifier: EPL-2.0 OR Apache-2.0

.source Unbalanced.j
.class public tr/test/MonitorElimination/Unbalanced
.super java/lang/Object

; specify the initializer method
.method public <init>()V
    ; just call Object's initializer
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

; static method that locks an object without unlocking it
.method public static unbalancedMonenter(Ljava/lang/Object;)V
    .limit stack 1

    aload_0
    monitorenter

    return
.end method

; static method that unlocks an object without first locking it
.method public static unbalancedMonexit(Ljava/lang/Object;)V
    .limit stack 1

    aload_0
    monitorexit

    return
.end method