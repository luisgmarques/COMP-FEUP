.class public Life
.super java/lang/Object

.field public UNDERPOP_LIM I
.field public OVERPOP_LIM I
.field public REPRODUCE_NUM I
.field public LOOPS_PER_MS I
.field public xMax I
.field public yMax I
.field public field_ESC [I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 5
	.limit locals 2
	new Life
	dup
	invokespecial Life/<init>()V
	astore_1
	aload_1
	invokevirtual Life/init()Z
	pop
L1:
	aload_1
	invokevirtual Life/printField()Z
	pop
	aload_1
	invokevirtual Life/update()Z
	pop
	iconst_1
	if_icmplt L1
	return
.end method

.method public init()Z
	.limit stack 3
	.limit locals 3
	iconst_1
	newarray int
	astore_2
	iconst_2
	aload_0
	swap
	putfield Life/UNDERPOP_LIM I
	iconst_3
	aload_0
	swap
	putfield Life/OVERPOP_LIM I
	iconst_3
	aload_0
	swap
	putfield Life/REPRODUCE_NUM I
	ldc 225000
	aload_0
	swap
	putfield Life/LOOPS_PER_MS I
	aload_0
	aload_2
	invokevirtual Life/field([I)[I
	aload_0
	swap
	putfield Life/field_ESC [I
	aload_2
	iconst_0
	iaload
	istore_1
	iload_1
	iconst_1
	isub
	aload_0
	swap
	putfield Life/xMax I
	aload_0
	getfield Life/field_ESC [I
	arraylength
	iload_1
	idiv
	iconst_1
	isub
	aload_0
	swap
	putfield Life/yMax I
	iconst_1
	ireturn
.end method

.method public field([I)[I
	.limit stack 3
	.limit locals 3
	bipush 100
	newarray int
	astore_1
	aload_1
	iconst_0
	bipush 10
	iastore
	aload_1
	iconst_0
	iconst_0
	iastore
	aload_1
	iconst_1
	iconst_0
	iastore
	aload_1
	iconst_2
	iconst_1
	iastore
	aload_1
	iconst_3
	iconst_0
	iastore
	aload_1
	iconst_4
	iconst_0
	iastore
	aload_1
	iconst_5
	iconst_0
	iastore
	aload_1
	bipush 6
	iconst_0
	iastore
	aload_1
	bipush 7
	iconst_0
	iastore
	aload_1
	bipush 8
	iconst_0
	iastore
	aload_1
	bipush 9
	iconst_0
	iastore
	aload_1
	bipush 10
	iconst_1
	iastore
	aload_1
	bipush 11
	iconst_0
	iastore
	aload_1
	bipush 12
	iconst_1
	iastore
	aload_1
	bipush 13
	iconst_0
	iastore
	aload_1
	bipush 14
	iconst_0
	iastore
	aload_1
	bipush 15
	iconst_0
	iastore
	aload_1
	bipush 16
	iconst_0
	iastore
	aload_1
	bipush 17
	iconst_0
	iastore
	aload_1
	bipush 18
	iconst_0
	iastore
	aload_1
	bipush 19
	iconst_0
	iastore
	aload_1
	bipush 20
	iconst_0
	iastore
	aload_1
	bipush 21
	iconst_1
	iastore
	aload_1
	bipush 22
	iconst_1
	iastore
	aload_1
	bipush 23
	iconst_0
	iastore
	aload_1
	bipush 24
	iconst_0
	iastore
	aload_1
	bipush 25
	iconst_0
	iastore
	aload_1
	bipush 26
	iconst_0
	iastore
	aload_1
	bipush 27
	iconst_0
	iastore
	aload_1
	bipush 28
	iconst_0
	iastore
	aload_1
	bipush 29
	iconst_0
	iastore
	aload_1
	bipush 30
	iconst_0
	iastore
	aload_1
	bipush 31
	iconst_0
	iastore
	aload_1
	bipush 32
	iconst_0
	iastore
	aload_1
	bipush 33
	iconst_0
	iastore
	aload_1
	bipush 34
	iconst_0
	iastore
	aload_1
	bipush 35
	iconst_0
	iastore
	aload_1
	bipush 36
	iconst_0
	iastore
	aload_1
	bipush 37
	iconst_0
	iastore
	aload_1
	bipush 38
	iconst_0
	iastore
	aload_1
	bipush 39
	iconst_0
	iastore
	aload_1
	bipush 40
	iconst_0
	iastore
	aload_1
	bipush 41
	iconst_0
	iastore
	aload_1
	bipush 42
	iconst_0
	iastore
	aload_1
	bipush 43
	iconst_0
	iastore
	aload_1
	bipush 44
	iconst_0
	iastore
	aload_1
	bipush 45
	iconst_0
	iastore
	aload_1
	bipush 46
	iconst_0
	iastore
	aload_1
	bipush 47
	iconst_0
	iastore
	aload_1
	bipush 48
	iconst_0
	iastore
	aload_1
	bipush 49
	iconst_0
	iastore
	aload_1
	bipush 50
	iconst_0
	iastore
	aload_1
	bipush 51
	iconst_0
	iastore
	aload_1
	bipush 52
	iconst_0
	iastore
	aload_1
	bipush 53
	iconst_0
	iastore
	aload_1
	bipush 54
	iconst_0
	iastore
	aload_1
	bipush 55
	iconst_0
	iastore
	aload_1
	bipush 56
	iconst_0
	iastore
	aload_1
	bipush 57
	iconst_0
	iastore
	aload_1
	bipush 58
	iconst_0
	iastore
	aload_1
	bipush 59
	iconst_0
	iastore
	aload_1
	bipush 60
	iconst_0
	iastore
	aload_1
	bipush 61
	iconst_0
	iastore
	aload_1
	bipush 62
	iconst_0
	iastore
	aload_1
	bipush 63
	iconst_0
	iastore
	aload_1
	bipush 64
	iconst_0
	iastore
	aload_1
	bipush 65
	iconst_0
	iastore
	aload_1
	bipush 66
	iconst_0
	iastore
	aload_1
	bipush 67
	iconst_0
	iastore
	aload_1
	bipush 68
	iconst_0
	iastore
	aload_1
	bipush 69
	iconst_0
	iastore
	aload_1
	bipush 70
	iconst_0
	iastore
	aload_1
	bipush 71
	iconst_0
	iastore
	aload_1
	bipush 72
	iconst_0
	iastore
	aload_1
	bipush 73
	iconst_0
	iastore
	aload_1
	bipush 74
	iconst_0
	iastore
	aload_1
	bipush 75
	iconst_0
	iastore
	aload_1
	bipush 76
	iconst_0
	iastore
	aload_1
	bipush 77
	iconst_0
	iastore
	aload_1
	bipush 78
	iconst_0
	iastore
	aload_1
	bipush 79
	iconst_0
	iastore
	aload_1
	bipush 80
	iconst_0
	iastore
	aload_1
	bipush 81
	iconst_0
	iastore
	aload_1
	bipush 82
	iconst_0
	iastore
	aload_1
	bipush 83
	iconst_0
	iastore
	aload_1
	bipush 84
	iconst_0
	iastore
	aload_1
	bipush 85
	iconst_0
	iastore
	aload_1
	bipush 86
	iconst_0
	iastore
	aload_1
	bipush 87
	iconst_0
	iastore
	aload_1
	bipush 88
	iconst_0
	iastore
	aload_1
	bipush 89
	iconst_0
	iastore
	aload_1
	bipush 90
	iconst_0
	iastore
	aload_1
	bipush 91
	iconst_0
	iastore
	aload_1
	bipush 92
	iconst_0
	iastore
	aload_1
	bipush 93
	iconst_0
	iastore
	aload_1
	bipush 94
	iconst_0
	iastore
	aload_1
	bipush 95
	iconst_0
	iastore
	aload_1
	bipush 96
	iconst_0
	iastore
	aload_1
	bipush 97
	iconst_0
	iastore
	aload_1
	bipush 98
	iconst_0
	iastore
	aload_1
	bipush 99
	iconst_0
	iastore
	aload_1
	areturn
.end method

.method public update()Z
	.limit stack 12
	.limit locals 6
	aload_0
	getfield Life/field_ESC [I
	arraylength
	newarray int
	astore_1
	iconst_0
	istore 4
L2:
	iload 4
	aload_0
	getfield Life/field_ESC [I
	arraylength
	if_icmpge L2_end
	aload_0
	getfield Life/field_ESC [I
	iload 4
	iaload
	istore_3
	aload_0
	iload 4
	invokevirtual Life/getLiveNeighborN(I)I
	istore_2
	iload_3
	iconst_1
	if_icmpge L3
	iconst_1
	goto L4
L3:
	iconst_0
L4:
	iconst_1
	ixor
	iconst_1
	if_icmplt L5
L9:
	aload_0
	iload_2
	aload_0
	getfield Life/UNDERPOP_LIM I
	invokevirtual Life/ge(II)Z
	iconst_1
	if_icmpeq L7
	aload_0
	iload_2
	aload_0
	getfield Life/UNDERPOP_LIM I
	invokevirtual Life/ge(II)Z
	goto L8
L7:
	aload_0
	iload_2
	aload_0
	getfield Life/UNDERPOP_LIM I
	invokevirtual Life/ge(II)Z
	aload_0
	iload_2
	aload_0
	getfield Life/OVERPOP_LIM I
	invokevirtual Life/le(II)Z
	iand
L8:
	istore_2
	iload_2
	iconst_1
	ixor
	iconst_1
	if_icmplt L10
	aload_1
	iload 4
	iconst_0
	iastore
	goto L11
L10:
	aload_1
	iload 4
	aload_0
	getfield Life/field_ESC [I
	iload 4
	iaload
	iastore
L11:
	goto L11
L5:
	aload_0
	iload_2
	aload_0
	getfield Life/REPRODUCE_NUM I
	invokevirtual Life/eq(II)Z
	iconst_1
	if_icmplt L12
	aload_1
	iload 4
	iconst_1
	iastore
	goto L11
L12:
	aload_1
	iload 4
	aload_0
	getfield Life/field_ESC [I
	iload 4
	iaload
	iastore
L11:
L11:
	iinc 4 1
	goto L2
L2_end:
	aload_1
	aload_0
	swap
	putfield Life/field_ESC [I
	iconst_1
	ireturn
.end method

.method public printField()Z
	.limit stack 6
	.limit locals 3
	iconst_0
	istore_2
	iconst_0
	istore_1
L13:
	iload_2
	aload_0
	getfield Life/field_ESC [I
	arraylength
	if_icmpge L13_end
	aload_0
	iload_1
	aload_0
	getfield Life/xMax I
	invokevirtual Life/gt(II)Z
	iconst_1
	if_icmplt L14
	invokestatic io/println()V
	iconst_0
	istore_1
	goto L15
L14:
L15:
	aload_0
	getfield Life/field_ESC [I
	iload_2
	iaload
	invokestatic io/print(I)V
	iinc 2 1
	iinc 1 1
	goto L13
L13_end:
	invokestatic io/println()V
	invokestatic io/println()V
	iconst_1
	ireturn
.end method

.method public trIdx(II)I
	.limit stack 3
	.limit locals 3
	iload_1
	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	iload_2
	imul
	iadd
	ireturn
.end method

.method public cartIdx(I)[I
	.limit stack 3
	.limit locals 6
	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	istore_3
	iload_1
	iload_3
	idiv
	istore 4
	iload_1
	iload 4
	iload_3
	imul
	isub
	istore_1
	iconst_2
	newarray int
	astore_2
	aload_2
	iconst_0
	iload_1
	iastore
	aload_2
	iconst_1
	iload 4
	iastore
	aload_2
	areturn
.end method

.method public getNeighborCoords(I)[I
	.limit stack 8
	.limit locals 10
	aload_0
	iload_1
	invokevirtual Life/cartIdx(I)[I
	astore_2
	aload_2
	iconst_0
	iaload
	istore 7
	aload_2
	iconst_1
	iaload
	istore_1
	iload 7
	aload_0
	getfield Life/xMax I
	if_icmpge L16
	iload 7
	iconst_1
	iadd
	istore 4
	aload_0
	iload 7
	iconst_0
	invokevirtual Life/gt(II)Z
	iconst_1
	if_icmplt L18
	iload 7
	iconst_1
	isub
	istore 6
	goto L19
L18:
	aload_0
	getfield Life/xMax I
	istore 6
L19:
	goto L19
L16:
	iconst_0
	istore 4
	iload 7
	iconst_1
	isub
	istore 6
L19:
	iload_1
	aload_0
	getfield Life/yMax I
	if_icmpge L20
	iload_1
	iconst_1
	iadd
	istore_3
	aload_0
	iload_1
	iconst_0
	invokevirtual Life/gt(II)Z
	iconst_1
	if_icmplt L22
	iload_1
	iconst_1
	isub
	istore 5
	goto L23
L22:
	aload_0
	getfield Life/yMax I
	istore 5
L23:
	goto L23
L20:
	iconst_0
	istore_3
	iload_1
	iconst_1
	isub
	istore 5
L23:
	bipush 8
	newarray int
	astore_2
	aload_2
	iconst_0
	aload_0
	iload 7
	iload 5
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	iconst_1
	aload_0
	iload 6
	iload 5
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	iconst_2
	aload_0
	iload 6
	iload_1
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	iconst_3
	aload_0
	iload 6
	iload_3
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	iconst_4
	aload_0
	iload 7
	iload_3
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	iconst_5
	aload_0
	iload 4
	iload_3
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	bipush 6
	aload_0
	iload 4
	iload_1
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	bipush 7
	aload_0
	iload 4
	iload 5
	invokevirtual Life/trIdx(II)I
	iastore
	aload_2
	areturn
.end method

.method public getLiveNeighborN(I)I
	.limit stack 7
	.limit locals 5
	iconst_0
	istore_2
	aload_0
	iload_1
	invokevirtual Life/getNeighborCoords(I)[I
	astore_3
	iconst_0
	istore_1
L24:
	iload_1
	aload_3
	arraylength
	if_icmpge L24_end
	aload_0
	aload_0
	getfield Life/field_ESC [I
	aload_3
	iload_1
	iaload
	iaload
	iconst_0
	invokevirtual Life/ne(II)Z
	iconst_1
	if_icmplt L25
	iinc 2 1
	goto L26
L25:
L26:
	iinc 1 1
	goto L24
L24_end:
	iload_2
	ireturn
.end method

.method public busyWait(I)Z
	.limit stack 2
	.limit locals 4
	iload_1
	aload_0
	getfield Life/LOOPS_PER_MS I
	imul
	istore_1
	iconst_0
	istore_2
L27:
	iload_2
	iload_1
	if_icmpge L27_end
	iinc 2 1
	goto L27
L27_end:
	iconst_1
	ireturn
.end method

.method public eq(II)Z
	.limit stack 6
	.limit locals 3
L30:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L28
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	goto L29
L28:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	aload_0
	iload_2
	iload_1
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	iand
L29:
	ireturn
.end method

.method public ne(II)Z
	.limit stack 3
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	ireturn
.end method

.method public lt(II)Z
	.limit stack 2
	.limit locals 3
	iload_1
	iload_2
	if_icmpge L31
	iconst_1
	goto L32
L31:
	iconst_0
L32:
	ireturn
.end method

.method public le(II)Z
	.limit stack 6
	.limit locals 3
L35:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L33
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	goto L34
L33:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z
	iconst_1
	ixor
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	iand
L34:
	iconst_1
	ixor
	ireturn
.end method

.method public gt(II)Z
	.limit stack 3
	.limit locals 3
	aload_0
	iload_1
	iload_2
	invokevirtual Life/le(II)Z
	iconst_1
	ixor
	ireturn
.end method

.method public ge(II)Z
	.limit stack 6
	.limit locals 3
L38:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/gt(II)Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L36
	aload_0
	iload_1
	iload_2
	invokevirtual Life/gt(II)Z
	iconst_1
	ixor
	goto L37
L36:
	aload_0
	iload_1
	iload_2
	invokevirtual Life/gt(II)Z
	iconst_1
	ixor
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z
	iconst_1
	ixor
	iand
L37:
	iconst_1
	ixor
	ireturn
.end method

