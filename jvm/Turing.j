.class public Turing
.super java/lang/Object

.field public NUM_SYMBOLS I
.field public NUM_STATES I
.field public WTABLE [I
.field public MTABLE [I
.field public NTABLE [I
.field public H I
.field public L I
.field public R I
.field public TAPE [I
.field public curState I
.field public curPos I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 2
	new Turing
	dup
	invokespecial Turing/<init>()V
	astore_1
	aload_1
	invokevirtual Turing/init_bb_3s2sy()Z
	pop
	aload_1
	invokevirtual Turing/run()Z
	pop
	return
.end method

.method public init_bb_3s2sy()Z
	.limit stack 11
	.limit locals 1
	aload_0
	iconst_3
	iconst_2
	bipush 18
	invokevirtual Turing/initGeneric(III)[I
	aload_0
	swap
	putfield Turing/TAPE [I
	aload_0
	iconst_0
	iconst_0
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_1
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_2
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_0
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_2
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_1
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_2
	iconst_1
	aload_0
	getfield Turing/R I
	aload_0
	getfield Turing/H I
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	iconst_1
	ireturn
.end method

.method public init_bb_4s2sy()Z
	.limit stack 13
	.limit locals 1
	aload_0
	iconst_4
	iconst_2
	bipush 20
	invokevirtual Turing/initGeneric(III)[I
	aload_0
	swap
	putfield Turing/TAPE [I
	aload_0
	iconst_0
	iconst_0
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_1
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_2
	iconst_1
	aload_0
	getfield Turing/R I
	aload_0
	getfield Turing/H I
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_3
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_3
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_0
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_1
	iconst_0
	aload_0
	getfield Turing/L I
	iconst_2
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_2
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_3
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_3
	iconst_0
	aload_0
	getfield Turing/R I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	iconst_1
	ireturn
.end method

.method public run()Z
	.limit stack 4
	.limit locals 4
	aload_0
	astore_3
	iconst_0
	istore_2
L1:
	iload_2
	iconst_1
	ixor
	iconst_1
	if_icmplt L1_end
	aload_0
	invokevirtual Turing/printTape()Z
	pop
	invokestatic io/read()I
	istore_1
	aload_3
	invokevirtual Turing/trans()Z
	iconst_1
	ixor
	istore_2
	goto L1
L1_end:
	aload_0
	invokevirtual Turing/printTape()Z
	pop
	iconst_1
	ireturn
.end method

.method public printTape()Z
	.limit stack 10
	.limit locals 2
	iconst_0
	istore_1
L2:
	iload_1
	aload_0
	getfield Turing/TAPE [I
	arraylength
	if_icmpge L2_end
L5:
	iload_1
	aload_0
	getfield Turing/curPos I
	if_icmpge L6
	iconst_1
	goto L7
L6:
	iconst_0
L7:
	iconst_1
	ixor
	iconst_1
	if_icmpeq L3
	iload_1
	aload_0
	getfield Turing/curPos I
	if_icmpge L8
	iconst_1
	goto L9
L8:
	iconst_0
L9:
	iconst_1
	ixor
	goto L4
L3:
	iload_1
	aload_0
	getfield Turing/curPos I
	if_icmpge L10
	iconst_1
	goto L11
L10:
	iconst_0
L11:
	iconst_1
	ixor
	aload_0
	getfield Turing/curPos I
	iload_1
	if_icmpge L12
	iconst_1
	goto L13
L12:
	iconst_0
L13:
	iconst_1
	ixor
	iand
L4:
	iconst_1
	ixor
	iconst_1
	if_icmplt L14
	iconst_0
	invokestatic io/print(I)V
	goto L15
L14:
	aload_0
	getfield Turing/curState I
	iconst_1
	iadd
	invokestatic io/print(I)V
L15:
	iinc 1 1
	goto L2
L2_end:
	invokestatic io/println()V
	iconst_0
	istore_1
L16:
	iload_1
	aload_0
	getfield Turing/TAPE [I
	arraylength
	if_icmpge L16_end
	aload_0
	getfield Turing/TAPE [I
	iload_1
	iaload
	invokestatic io/print(I)V
	iinc 1 1
	goto L16
L16_end:
	invokestatic io/println()V
	invokestatic io/println()V
	iconst_1
	ireturn
.end method

.method public trans()Z
	.limit stack 9
	.limit locals 6
	aload_0
	getfield Turing/TAPE [I
	aload_0
	getfield Turing/curPos I
	iaload
	istore 4
	aload_0
	getfield Turing/WTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore_1
	aload_0
	getfield Turing/MTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore_2
	aload_0
	getfield Turing/NTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore_3
	aload_0
	getfield Turing/TAPE [I
	aload_0
	getfield Turing/curPos I
	iload_1
	iastore
	aload_0
	getfield Turing/curPos I
	iload_2
	iadd
	aload_0
	swap
	putfield Turing/curPos I
	iload_3
	aload_0
	swap
	putfield Turing/curState I
L19:
	aload_0
	getfield Turing/H I
	aload_0
	getfield Turing/curState I
	if_icmpge L20
	iconst_1
	goto L21
L20:
	iconst_0
L21:
	iconst_1
	ixor
	iconst_1
	if_icmpeq L17
	aload_0
	getfield Turing/H I
	aload_0
	getfield Turing/curState I
	if_icmpge L22
	iconst_1
	goto L23
L22:
	iconst_0
L23:
	iconst_1
	ixor
	goto L18
L17:
	aload_0
	getfield Turing/H I
	aload_0
	getfield Turing/curState I
	if_icmpge L24
	iconst_1
	goto L25
L24:
	iconst_0
L25:
	iconst_1
	ixor
	aload_0
	getfield Turing/curState I
	aload_0
	getfield Turing/H I
	if_icmpge L26
	iconst_1
	goto L27
L26:
	iconst_0
L27:
	iconst_1
	ixor
	iand
L18:
	iconst_1
	if_icmplt L28
	iconst_0
	istore 5
	goto L29
L28:
	iconst_1
	istore 5
L29:
	iload 5
	ireturn
.end method

.method public initGeneric(III)[I
	.limit stack 2
	.limit locals 6
	iload_2
	aload_0
	swap
	putfield Turing/NUM_SYMBOLS I
	iload_1
	aload_0
	swap
	putfield Turing/NUM_STATES I
	aload_0
	getfield Turing/NUM_SYMBOLS I
	aload_0
	getfield Turing/NUM_STATES I
	imul
	istore 5
	iconst_0
	iconst_1
	isub
	aload_0
	swap
	putfield Turing/H I
	iconst_0
	iconst_1
	isub
	aload_0
	swap
	putfield Turing/L I
	iconst_1
	aload_0
	swap
	putfield Turing/R I
	iload 5
	newarray int
	aload_0
	swap
	putfield Turing/WTABLE [I
	iload 5
	newarray int
	aload_0
	swap
	putfield Turing/MTABLE [I
	iload 5
	newarray int
	aload_0
	swap
	putfield Turing/NTABLE [I
	iload_3
	newarray int
	astore 4
	iconst_0
	aload_0
	swap
	putfield Turing/curState I
	aload 4
	arraylength
	iconst_2
	idiv
	aload_0
	swap
	putfield Turing/curPos I
	aload 4
	areturn
.end method

.method public ss2i(II)I
	.limit stack 2
	.limit locals 3
	iload_1
	aload_0
	getfield Turing/NUM_STATES I
	imul
	iload_2
	iadd
	ireturn
.end method

.method public setTrans(IIIII)Z
	.limit stack 4
	.limit locals 6
	aload_0
	getfield Turing/WTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload_3
	iastore
	aload_0
	getfield Turing/MTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload 4
	iastore
	aload_0
	getfield Turing/NTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload 5
	iastore
	iconst_1
	ireturn
.end method

