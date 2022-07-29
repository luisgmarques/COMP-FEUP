.class public Lazysort
.super Quicksort


.method public <init>()V
	aload_0
	invokespecial Quicksort/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 5
	.limit locals 5
	bipush 10
	newarray int
	astore_2
	iconst_0
	istore_1
L1:
	aload_2
	iload_1
	aload_2
	arraylength
	iload_1
	isub
	iastore
	iinc 1 1
	iload_1
	aload_2
	arraylength
	if_icmplt L1
	new Lazysort
	dup
	invokespecial Lazysort/<init>()V
	astore_1
	aload_1
	aload_2
	invokevirtual Lazysort/quicksort([I)Z
	pop
	iinc 1 2
	return
.end method

.method public quicksort([I)Z
	.limit stack 6
	.limit locals 3
	iconst_0
	iconst_5
	invokestatic MathUtils/random(II)I
	iconst_4
	if_icmpge L2
	aload_0
	aload_1
	invokevirtual Lazysort/beLazy([I)Z
	pop
	iconst_1
	istore_2
	goto L3
L2:
	iconst_0
	istore_2
L3:
	iload_2
	iconst_1
	if_icmplt L4
	iinc 2 1
	goto L5
L4:
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual Quicksort/quicksort([III)Z
	istore_2
L5:
	iload_2
	ireturn
.end method

.method public beLazy([I)Z
	.limit stack 6
	.limit locals 4
	aload_1
	arraylength
	istore_3
	iconst_0
	istore_2
L6:
	iload_2
	iload_3
	iconst_2
	idiv
	if_icmpge L6_end
	aload_1
	iload_2
	iconst_0
	bipush 10
	invokestatic MathUtils/random(II)I
	iastore
	iinc 2 1
	goto L6
L6_end:
L7:
	iload_2
	iload_3
	if_icmpge L7_end
	aload_1
	iload_2
	iconst_0
	bipush 10
	invokestatic MathUtils/random(II)I
	iconst_1
	iadd
	iastore
	iinc 2 1
	goto L7
L7_end:
	iconst_1
	ireturn
.end method

