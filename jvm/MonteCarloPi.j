.class public MonteCarloPi
.super java/lang/Object


.method public performSingleEstimate()Z
	.limit stack 3
	.limit locals 5
	iconst_0
	bipush 100
	isub
	bipush 100
	invokestatic MathUtils/random(II)I
	istore_2
	iconst_0
	bipush 100
	isub
	bipush 100
	invokestatic MathUtils/random(II)I
	istore_1
	iload_2
	iload_2
	imul
	iload_1
	iload_1
	imul
	iadd
	bipush 100
	idiv
	istore_1
	iload_1
	bipush 100
	if_icmpge L1
	iconst_1
	istore_1
	goto L2
L1:
	iconst_0
	istore_1
L2:
	iload_1
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 4
	.limit locals 5
	iconst_0
	istore_2
	iconst_0
	istore_3
L3:
	iload_2
	iload_1
	if_icmpge L3_end
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	iconst_1
	if_icmplt L4
	iinc 3 1
	goto L5
L4:
L5:
	iinc 2 1
	goto L3
L3_end:
	sipush 400
	iload_3
	imul
	iload_1
	idiv
	istore_1
	iload_1
	ireturn
.end method

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 3
	invokestatic ioPlus/requestNumber()I
	istore_1
	new MonteCarloPi
	dup
	invokespecial MonteCarloPi/<init>()V
	iload_1
	invokevirtual MonteCarloPi/estimatePi100(I)I
	istore_1
	iload_1
	invokestatic ioPlus/printResult(I)V
	return
.end method

