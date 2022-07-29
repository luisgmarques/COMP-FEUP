.class public TTT
.super java/lang/Object

.field public grid [I
.field public player I
.field public GRID_SIZE I
.field public winner I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 2
	new TTT
	dup
	invokespecial TTT/<init>()V
	astore_1
	aload_1
	invokevirtual TTT/initialize()I
	pop
	aload_1
	invokevirtual TTT/startGame()I
	pop
	return
.end method

.method public initialize()I
	.limit stack 5
	.limit locals 3
	iconst_0
	istore_1
	iconst_1
	aload_0
	swap
	putfield TTT/player I
	bipush 9
	aload_0
	swap
	putfield TTT/GRID_SIZE I
	aload_0
	getfield TTT/GRID_SIZE I
	newarray int
	aload_0
	swap
	putfield TTT/grid [I
L1:
	iload_1
	aload_0
	getfield TTT/GRID_SIZE I
	if_icmpge L1_end
	aload_0
	getfield TTT/grid [I
	iload_1
	iconst_0
	iastore
	iinc 1 1
	goto L1
L1_end:
	iconst_0
	istore_1
	aload_0
	invokevirtual TTT/printState()I
	pop
	iconst_0
	ireturn
.end method

.method public startGame()I
	.limit stack 7
	.limit locals 3
	iconst_0
	istore_1
	iconst_0
	aload_0
	swap
	putfield TTT/winner I
L2:
	iload_1
	aload_0
	getfield TTT/GRID_SIZE I
	if_icmpge L2_end
	aload_0
	invokevirtual TTT/readMove()I
	istore_2
	aload_0
	iload_2
	invokevirtual TTT/executeMove(I)I
	pop
	aload_0
	invokevirtual TTT/printState()I
	pop
	iinc 1 1
	aload_0
	invokevirtual TTT/gameOver()Z
	iconst_1
	if_icmplt L3
	bipush 9
	istore_1
L3:
	goto L2
L2_end:
	aload_0
	getfield TTT/winner I
	invokestatic io/println(I)V
	iconst_0
	ireturn
.end method

.method public gameOver()Z
	.limit stack 68
	.limit locals 3
	iconst_0
	istore_1
	aload_0
	getfield TTT/player I
	iconst_2
	if_icmpge L5
	iconst_2
	istore_2
	goto L6
L5:
	iconst_1
	istore_2
L6:
L9:
L12:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L10
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L11
L10:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L11:
	iconst_1
	if_icmpeq L7
L15:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L13
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L14
L13:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L14:
	goto L8
L7:
L18:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L16
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L17
L16:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L17:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L8:
	iconst_1
	if_icmplt L19
	iconst_1
	istore_1
L19:
L23:
L26:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L24
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L25
L24:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L25:
	iconst_1
	if_icmpeq L21
L29:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L27
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L28
L27:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L28:
	goto L22
L21:
L32:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L30
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L31
L30:
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L31:
	aload_0
	iconst_5
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L22:
	iconst_1
	if_icmplt L33
	iconst_1
	istore_1
L33:
L37:
L40:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L38
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L39
L38:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	bipush 7
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L39:
	iconst_1
	if_icmpeq L35
L43:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L41
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L42
L41:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	bipush 7
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L42:
	goto L36
L35:
L46:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L44
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L45
L44:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	bipush 7
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L45:
	aload_0
	bipush 8
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L36:
	iconst_1
	if_icmplt L47
	iconst_1
	istore_1
L47:
L51:
L54:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L52
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L53
L52:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L53:
	iconst_1
	if_icmpeq L49
L57:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L55
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L56
L55:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L56:
	goto L50
L49:
L60:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L58
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L59
L58:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_3
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L59:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L50:
	iconst_1
	if_icmplt L61
	iconst_1
	istore_1
L61:
L65:
L68:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L66
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L67
L66:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L67:
	iconst_1
	if_icmpeq L63
L71:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L69
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L70
L69:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L70:
	goto L64
L63:
L74:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L72
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L73
L72:
	aload_0
	iconst_1
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L73:
	aload_0
	bipush 7
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L64:
	iconst_1
	if_icmplt L75
	iconst_1
	istore_1
L75:
L79:
L82:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L80
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L81
L80:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_5
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L81:
	iconst_1
	if_icmpeq L77
L85:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L83
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L84
L83:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_5
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L84:
	goto L78
L77:
L88:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L86
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L87
L86:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_5
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L87:
	aload_0
	bipush 8
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L78:
	iconst_1
	if_icmplt L89
	iconst_1
	istore_1
L89:
L93:
L96:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L94
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L95
L94:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L95:
	iconst_1
	if_icmpeq L91
L99:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L97
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L98
L97:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L98:
	goto L92
L91:
L102:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L100
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L101
L100:
	aload_0
	iconst_0
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L101:
	aload_0
	bipush 8
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L92:
	iconst_1
	if_icmplt L103
	iconst_1
	istore_1
L103:
L107:
L110:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L108
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L109
L108:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L109:
	iconst_1
	if_icmpeq L105
L113:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L111
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L112
L111:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L112:
	goto L106
L105:
L116:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmpeq L114
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	goto L115
L114:
	aload_0
	iconst_2
	iload_2
	invokevirtual TTT/cellContains(II)Z
	aload_0
	iconst_4
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L115:
	aload_0
	bipush 6
	iload_2
	invokevirtual TTT/cellContains(II)Z
	iand
L106:
	iconst_1
	if_icmplt L117
	iconst_1
	istore_1
L117:
	iload_1
	iconst_1
	if_icmplt L119
	iload_2
	aload_0
	swap
	putfield TTT/winner I
L119:
	iload_1
	ireturn
.end method

.method public executeMove(I)I
	.limit stack 3
	.limit locals 2
	aload_0
	getfield TTT/grid [I
	iload_1
	iconst_1
	isub
	aload_0
	getfield TTT/player I
	iastore
	aload_0
	getfield TTT/player I
	iconst_2
	if_icmpge L121
	iconst_2
	aload_0
	swap
	putfield TTT/player I
	goto L122
L121:
	iconst_1
	aload_0
	swap
	putfield TTT/player I
L122:
	iconst_0
	ireturn
.end method

.method public isGridFull()Z
	.limit stack 5
	.limit locals 3
	iconst_1
	istore_2
	iconst_0
	istore_1
L123:
	iload_1
	aload_0
	getfield TTT/GRID_SIZE I
	if_icmpge L123_end
	aload_0
	iload_1
	iconst_0
	invokevirtual TTT/cellContains(II)Z
	iconst_1
	if_icmplt L124
	iconst_0
	istore_2
L124:
	iinc 1 1
	goto L123
L123_end:
	iload_2
	ireturn
.end method

.method public printState()I
	.limit stack 4
	.limit locals 1
	aload_0
	invokevirtual TTT/isGridFull()Z
	iconst_1
	if_icmplt L126
	iconst_1
	invokestatic io/println(I)V
	goto L127
L126:
	iconst_0
	invokestatic io/println(I)V
L127:
	aload_0
	invokevirtual TTT/printGrid()I
	pop
	iconst_0
	ireturn
.end method

.method public printGrid()I
	.limit stack 19
	.limit locals 1
	aload_0
	getfield TTT/grid [I
	iconst_0
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	iconst_1
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	iconst_2
	iaload
	invokestatic io/println(I)V
	aload_0
	getfield TTT/grid [I
	iconst_3
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	iconst_4
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	iconst_5
	iaload
	invokestatic io/println(I)V
	aload_0
	getfield TTT/grid [I
	bipush 6
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	bipush 7
	iaload
	invokestatic io/print(I)V
	aload_0
	getfield TTT/grid [I
	bipush 8
	iaload
	invokestatic io/println(I)V
	iconst_0
	ireturn
.end method

.method public readMove()I
	.limit stack 5
	.limit locals 3
	iconst_1
	istore_1
	iconst_0
	istore_2
L128:
	iload_2
	iconst_1
	if_icmpge L128_end
	invokestatic io/read()I
	istore_1
	aload_0
	iload_1
	invokevirtual TTT/isMoveValid(I)Z
	iconst_1
	if_icmplt L129
	iconst_1
	istore_2
L129:
	goto L128
L128_end:
	iload_1
	ireturn
.end method

.method public isMoveValid(I)Z
	.limit stack 8
	.limit locals 3
	iconst_0
	istore_2
L133:
	iload_1
	bipush 10
	if_icmpge L134
	iconst_1
	goto L135
L134:
	iconst_0
L135:
	iconst_1
	if_icmpeq L131
	iload_1
	bipush 10
	if_icmpge L136
	iconst_1
	goto L137
L136:
	iconst_0
L137:
	goto L132
L131:
	iload_1
	bipush 10
	if_icmpge L138
	iconst_1
	goto L139
L138:
	iconst_0
L139:
	iload_1
	iconst_1
	if_icmpge L140
	iconst_1
	goto L141
L140:
	iconst_0
L141:
	iconst_1
	ixor
	iand
L132:
	iconst_1
	if_icmplt L142
	aload_0
	getfield TTT/grid [I
	iload_1
	iconst_1
	isub
	iaload
	iconst_1
	if_icmpge L144
	iconst_1
	istore_2
L144:
L142:
	iload_2
	ireturn
.end method

.method public test()Z
	.limit stack 1
	.limit locals 1
	iconst_1
	ireturn
.end method

.method public cellContains(II)Z
	.limit stack 9
	.limit locals 3
L148:
	aload_0
	getfield TTT/grid [I
	iload_1
	iaload
	iload_2
	if_icmpge L149
	iconst_1
	goto L150
L149:
	iconst_0
L150:
	iconst_1
	ixor
	iconst_1
	if_icmpeq L146
	aload_0
	getfield TTT/grid [I
	iload_1
	iaload
	iload_2
	if_icmpge L151
	iconst_1
	goto L152
L151:
	iconst_0
L152:
	iconst_1
	ixor
	goto L147
L146:
	aload_0
	getfield TTT/grid [I
	iload_1
	iaload
	iload_2
	if_icmpge L153
	iconst_1
	goto L154
L153:
	iconst_0
L154:
	iconst_1
	ixor
	iload_2
	aload_0
	getfield TTT/grid [I
	iload_1
	iaload
	if_icmpge L155
	iconst_1
	goto L156
L155:
	iconst_0
L156:
	iconst_1
	ixor
	iand
L147:
	ireturn
.end method

