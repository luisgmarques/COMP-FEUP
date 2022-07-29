.class public Test
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 5
	.limit locals 6
	iconst_1
	newarray int
	astore 4
	iconst_2
	iconst_3
	iadd
	iconst_5
	iadd
	istore 5
	iconst_2
	iconst_3
	if_icmpge L1
	aload 4
	iconst_0
	iconst_1
	iastore
	aload 4
	arraylength	
	istore 5
	iload 5
	invokestatic io/println(I)V
	goto L2
L1:
L2:
	return
.end method

