.class public QuickSort
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 6
	.limit locals 4
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
	new QuickSort
	dup
	invokespecial QuickSort/<init>()V
	astore_1
	aload_1
	aload_2
	invokevirtual QuickSort/quicksort([I)Z
	pop
	aload_1
	aload_2
	invokevirtual QuickSort/printL([I)Z
	pop
	return
.end method

.method public printL([I)Z
	.limit stack 4
	.limit locals 3
	iconst_0
	istore_2
L2:
	iload_2
	aload_1
	arraylength
	if_icmpge L2_end
	aload_1
	iload_2
	iaload
	invokestatic io/println(I)V
	iinc 2 1
	goto L2
L2_end:
	iconst_1
	ireturn
.end method

.method public quicksort([I)Z
	.limit stack 5
	.limit locals 2
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual QuickSort/quicksort([III)Z
	ireturn
.end method

.method public quicksort([III)Z
	.limit stack 7
	.limit locals 5
	iload_2
	iload_3
	if_icmpge L3
	aload_0
	aload_1
	iload_2
	iload_3
	invokevirtual QuickSort/partition([III)I
	istore 4
	aload_0
	aload_1
	iload_2
	iload 4
	iconst_1
	isub
	invokevirtual QuickSort/quicksort([III)Z
	pop
	aload_0
	aload_1
	iload 4
	iconst_1
	iadd
	iload_3
	invokevirtual QuickSort/quicksort([III)Z
	pop
	goto L4
L3:
L4:
	iconst_1
	ireturn
.end method

.method public partition([III)I
	.limit stack 11
	.limit locals 8
	aload_1
	iload_3
	iaload
	istore 6
	iload_2
	istore 5
	iload_2
	istore 4
L5:
	iload 4
	iload_3
	if_icmpge L5_end
	aload_1
	iload 4
	iaload
	iload 6
	if_icmpge L6
	aload_1
	iload 5
	iaload
	istore_2
	aload_1
	iload 5
	aload_1
	iload 4
	iaload
	iastore
	aload_1
	iload 4
	iload_2
	iastore
	iinc 5 1
	goto L7
L6:
L7:
	iinc 4 1
	goto L5
L5_end:
	aload_1
	iload 5
	iaload
	istore_2
	aload_1
	iload 5
	aload_1
	iload_3
	iaload
	iastore
	aload_1
	iload_3
	iload_2
	iastore
	iload 5
	ireturn
.end method

