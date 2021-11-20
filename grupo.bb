
; ARREGLO DEL FILTRADO DE TEXTURAS

Const D3DTSS_MAGFILTER = 16
Const D3DTSS_MINFILTER = 17
Const D3DTSS_MIPFILTER = 18
Const D3DTEXF_NONE = 0 
Const D3DTEXF_POINT = 1
Const D3DTEXF_LINEAR = 2

; FIN DE LAS VARIABLES DEL ARREGLO DE FILTRADO DE TEXTURAS


Local resWidth#=Float  1280; Resolución horizontal
Local resHeight#=Float   720 ; Resolución vertical
Local resCd=32 ; Profundidad de color
Local resWindow=2 ; Modo ventana: 2, pantalla completa 1

AppTitle "Blitz3D Aquarium"
Graphics3D resWidth#, resHeight#, resCd, resWindow 
SeedRnd(MilliSecs())
SetBuffer BackBuffer() 

Local running=1 ; ¿Está el programa corriendo?
Local cubo=LoadMesh("juanv4.b3d") 




Local camara=CreateCamera()
Local habitacion=LoadMesh("suelo.3ds") 
Local luz=CreateLight(1)
Local texturaCubo = LoadTexture("juan.png")
Local texturaSuelo = LoadTexture("arena.png")
Local oldCubo = LoadTexture("cubo.png")
Local fpsTimer
Local fps
Local fpsTicks
Local logicTimer
Local logicTicks
Local scenCubo=CreateCube()

; Creación de variables globales, que pueden ser accedidas desde cualquier sitio

; Posicionamiento y rotación del cubo
Global posXCubo# = Float 0
Global posYCubo# = Float 0
Global posZCubo# = Float 5 
Global rotXCubo# = Float 0
Global rotYCubo# = Float 0
Global rotZCubo# = Float 0
Global rotando = 0
Global velocidadCubo# = 0.5
; Posicionamiento de la cámara
Global camX# = 0
Global camY# = 0 
Global camZ# = 0
Global camMode = 0 
Global cantidadBalas = 0
Global delayBalas = 0
Global maxFish = 50
Global activaTexto = 1
Global maxFishData = 3
Global currentMapChunk = 1

Dim colisions#(6)


;Global type_bala = 1
;Global type_fish = 2


EntityType cubo,type_bala

Dim datosPescados(maxFishData,2)
datosPescados(1,1) = LoadMesh("pescao.3ds") 
datosPescados(2,1) = LoadMesh("pescao2.3ds")
datosPescados(3,1) = LoadMesh("tiburon.3ds") 
datosPescados(1,2) = LoadTexture("pescao.png")
datosPescados(2,2) = LoadTexture("pescao2.png")
datosPescados(3,2) = LoadTexture("tiburon.png")


Dim datosMapa(2,7)
datosMapa(1,1) = "suelo.3ds"
datosMapa(1,2) = 80
datosMapa(1,3) = 80
datosMapa(1,4) = 80
datosMapa(1,5) = 0
datosMapa(1,6) = -15
datosMapa(1,7) = 10




datosMapa(2,1) = "suelo.3ds"
datosMapa(2,2) = 80
datosMapa(2,3) = 80
datosMapa(2,4) = 80
datosMapa(2,5) = 0
datosMapa(2,6) = -15
datosMapa(2,7) = 90


datosMapa(3,1) = "suelo.3ds"
datosMapa(3,2) = 80
datosMapa(3,3) = 80
datosMapa(3,4) = 80
datosMapa(3,5) = 0
datosMapa(3,6) = -15
datosMapa(3,7) = 170




For ifd=1 To maxFishData
	PositionEntity datosPescados(ifd,1),9999,9999,9999
Next


Dim datos2Pescados# (maxFishData,14)
datos2Pescados(1,1) = -32 ;MinX
datos2Pescados(1,2) = -48 ;MaxX
datos2Pescados(2,1) = -25
datos2Pescados(2,2) = -30
datos2Pescados(3,1) = -32
datos2Pescados(3,2) = -48 


datos2Pescados(1,3) = -0.5 	;MinY
datos2Pescados(1,4) = 8	    ;MaxY
datos2Pescados(2,3) = -0.5 	
datos2Pescados(2,4) = 3
datos2Pescados(3,3) = -0.5 	
datos2Pescados(3,4) = 8	


datos2Pescados(1,5) = -12 	;MinZ
datos2Pescados(1,6) = 22	    ;MaxZ 
datos2Pescados(2,5) = -12 
datos2Pescados(2,6) = 22	
datos2Pescados(3,5) = -12 
datos2Pescados(3,6) = 22   

datos2Pescados(1,7) = 0.1 	;Min speed ; Should be 0.1
datos2Pescados(1,8) = 0.5	    ;Max speed ;Should be 0.5
datos2Pescados(2,7) = 0.25  ;Should be 0.25
datos2Pescados(2,8) = 0.8 ;Should be 0.8
datos2Pescados(3,7) = 0.01 	;Should be 0.01
datos2Pescados(3,8) = 0.1	;Should be 0.1

datos2Pescados(1,9) = 8000 ;Spawn rate
datos2Pescados(2,9) = 6000
datos2Pescados(3,9) = 1

datos2Pescados(1,10) = 0.25 ;Min Size
datos2Pescados(2,10) = 0.30
datos2Pescados(3,10) = 2

datos2Pescados(1,11) = 1 ;Max Size
datos2Pescados(2,11) = 0.5
datos2Pescados(3,11) = 6

datos2Pescados(3,12) = 2.8 ; Hitbox X Scale
datos2Pescados(3,13) = 1.44681370886 ; Hitbox Y Scale
datos2Pescados(3,14) = 0.579921469776 ; Hitbox Z Scale

datos2Pescados(1,12) = 1.5 
datos2Pescados(1,13) = 1.06157181923
datos2Pescados(1,14) = 1.09757123671 

datos2Pescados(2,12) = 1 
datos2Pescados(2,13) = 1.05156902511
datos2Pescados(2,14) = 1.05156902511

;datosPescados(1,1) = CreateCube()
;datosPescados(2,1) = CreateCube()
;datosPescados(3,1) = CreateCube()
;PositionEntity datosPescados(1,1),5,0,0
;PositionEntity datosPescados(2,1),0,5,0
;PositionEntity datosPescados(3,1),0,0,5
;ScaleEntity datosPescados(1,1),datos2Pescados(1,12),datos2Pescados(1,13),datos2Pescados(1,14)
;ScaleEntity datosPescados(2,1),datos2Pescados(2,12),datos2Pescados(2,13),datos2Pescados(2,14)
;ScaleEntity datosPescados(3,1),datos2Pescados(3,12),datos2Pescados(3,13),datos2Pescados(3,14)




; Manipulación de entidades inicial

ScaleEntity habitacion,80.0,80.0,80.0
ScaleEntity cubo,4,4,4
;rotYCubo# = 90
TurnEntity cubo,0,-90,0
PositionEntity habitacion,0,-15,10
PositionEntity scenCubo,36.5951,-19.4532,22.5235
ScaleEntity scenCubo,0.75,0.75,0.75
EntityTexture scenCubo,oldCubo
;FlipMesh habitacion

;PositionEntity camara, posXCubo#-2 , posYCubo# + 2, posZCubo# - 5

EntityParent camara, cubo
;MoveEntity camara,2,2,2
RotateEntity camara,0,90,0
MoveEntity camara,-0.35,0.25,-0.5


MoveEntity camara,0,2,-8


CameraViewport camara,0,0,resWidth#,resHeight# 	
;CameraFogMode camara,1
;CameraFogRange camara,1,50
;CameraFogColor camara,1,60,117
;CameraClsColor camara,1,60,117
;CameraRange camara,1,60
EntityTexture habitacion,texturaSuelo

;Creación de las balas
Dim balasJugador#(3,6) ; Array de las balas del jugador
Dim balasJugadorModelo(3)
For i=1 To 3 
	balasJugadorModelo(i)=CreateCube() ;Creación de bala
	;EntityType balasJugador(i,6),type_bala
	For z=1 To 2
		balasJugador(i,z)=9999 ; Posición inicial
	Next
	ScaleEntity balasJugadorModelo(i),0.25,0.25,0.25
Next

; Podremos almacenar hasta 3 balas, y cada una tendrá 3 coordenadas de posición y una velocidad, y un último valor para saber si debe moverse o no, el último de verdad es para el modelo


Dim pescaos# (maxFish,7) ; Array de peces
;posX,posY,posZ,velocidad,IDentidad


;For i=1 To maxFish
	;pescaos(i,1) = Rnd(datos2Pescados(pescaos(i,5),1),datos2Pescados(pescaos(i,5),2))
	;pescaos(i,2) = Rnd(datos2Pescados(pescaos(i,5),3),datos2Pescados(pescaos(i,5),4))
	;pescaos(i,3) = Rnd(datos2Pescados(pescaos(i,5),5),datos2Pescados(pescaos(i,5),6))
	;pescaos(i,4) = Rnd(datos2Pescados(pescaos(i,5),7),datos2Pescados(pescaos(i,5),8))
	;pescaos(i,5) = Rand(1,2)
;Next





Dim pescaosEnt(maxFish,2) ; Array con los modelos y texturas de los pescaos

;For i=1 To maxFish
;	pescaosEnt(i,1) = CopyEntity(datosPescados(pescaos(i,5),1))
;	pescaosEnt(i,2) = datosPescados(pescaos(i,5),2)
;	EntityTexture pescaosEnt(i,1),pescaosEnt(i,2)
;	RotateEntity pescaosEnt(i,1),0,90,0
;Next








Function spawnFish(a,b)

			Local selected = 0 ; ¿Se ha podido elegir el modelo del pescado ya?
			Local randChance = 0 ; Esto se usa para generar la probabilidad de que salga el pescao
			Local escala# = 0 ; Escala de los peces


			If b=0 Then
				pescaos(a,1) = Rnd(datos2Pescados(pescaos(a,5),1),datos2Pescados(pescaos(a,5),2))
				pescaos(a,2) = Rnd(datos2Pescados(pescaos(a,5),3),datos2Pescados(pescaos(a,5),4))
				pescaos(a,3) = Rnd(datos2Pescados(pescaos(a,5),5),datos2Pescados(pescaos(a,5),6))
				pescaos(a,4) = Rnd(datos2Pescados(pescaos(a,5),7),datos2Pescados(pescaos(a,5),8))
				While selected = 0
					pescaos(a,5) = Rand(1,maxFishData)
					randChance = Rand(0,10000)
					If randChance <= datos2Pescados(pescaos(a,5),9) Then
						selected = 1
					End If
				Wend
				
				pescaosEnt(a,1) = CopyEntity(datosPescados(pescaos(a,5),1))
				;EntityType pescaosEnt(a,1),type_fish
				pescaosEnt(a,2) = datosPescados(pescaos(a,5),2)



			End If


			ScaleEntity pescaosEnt(a,1),0,0,0
			FreeEntity pescaosEnt(a,1)
			pescaosEnt(a,1) = CopyEntity(datosPescados(pescaos(a,5),1))
			pescaosEnt(a,2) = datosPescados(pescaos(a,5),2)
			EntityTexture pescaosEnt(a,1),pescaosEnt(a,2)
			RotateEntity pescaosEnt(a,1),0,90,0
			escala = Rnd(datos2Pescados(pescaos(a,5),10),datos2Pescados(pescaos(a,5),11))
			ScaleEntity pescaosEnt(a,1),escala,escala,escala
			pescaos(a,7) = escala
			


			pescaos(a,1) = Rnd(datos2Pescados(pescaos(a,5),1),datos2Pescados(pescaos(a,5),2))
			pescaos(a,2) = Rnd(datos2Pescados(pescaos(a,5),3),datos2Pescados(pescaos(a,5),4))
			pescaos(a,3) = Rnd(datos2Pescados(pescaos(a,5),5),datos2Pescados(pescaos(a,5),6))
			pescaos(a,4) = Rnd(datos2Pescados(pescaos(a,5),7),datos2Pescados(pescaos(a,5),8))
			While selected = 0
					pescaos(a,5) = Rand(1,maxFishData)
					randChance = Rand(0,10000)
					If randChance <= datos2Pescados(pescaos(a,5),9) Then
						selected = 1
					End If
			Wend
			
			

End Function

For i=1 To maxFish
			spawnFish(i,0)
Next



Function logicaJuego(cubo, luz, texturaCubo, camara, habitacion) 	; Aquí funcionará toda la lógica del juego, llamamos esta función en el bucle y se repite constantemente
														;Le daremos como argumentos las entidades LOCALES del cubo, la luz y la cámara, y la textura del cubo

	
	; CONTROLES DEL CUBO
	If KeyDown(200)
		MoveEntity cubo,-velocidadCubo#,0,0
	End If

	If KeyDown(208)
		MoveEntity cubo,+velocidadCubo#,0,0
	End If

	If KeyDown(203)
		MoveEntity cubo,0,0,-velocidadCubo#
	End If

	If KeyDown(205)
		MoveEntity cubo,0,0,+velocidadCubo#
	End If

	If KeyDown(78)
		velocidadCubo# = velocidadCubo# + 0.01
	End If

	If KeyDown(74)
		velocidadCubo# = velocidadCubo# - 0.01
	End If

	If KeyDown(16)
		;MoveEntity cubo,0,+velocidadCubo#,0
		TurnEntity cubo,- velocidadCubo# * 2.5,0,0
	End If

	If KeyDown(18)
		;MoveEntity cubo,0,-velocidadCubo#,0
		TurnEntity cubo,+ velocidadCubo# * 2.5,0,0
	End If

	If KeyDown(30)
		;rotYCubo# = rotYCubo# - velocidadCubo# * 2.5 ;- 0.5
		;camY# = camY# - velocidadCubo#
		TurnEntity cubo,0,+ velocidadCubo# * 2.5,0
	End If
	If KeyDown(32)
		;rotYCubo# = rotYCubo# + velocidadCubo# * 2.5 ;+ 0.5
		;camY# = camY# + velocidadCubo#
		TurnEntity cubo,0,- velocidadCubo# * 2.5,0
	End If

	If KeyDown(17)
		;rotZCubo# = rotZCubo# - velocidadCubo# * 2.5 ;- 0.5
		;camX# = camX# + velocidadCubo#
		TurnEntity cubo,0,0,- velocidadCubo# * 2.5

	End If

	If KeyDown(31)
		;rotZCubo# = rotZCubo# + velocidadCubo# * 2.5 ;+ 0.5
		;camX# = camX# - velocidadCubo#
		TurnEntity cubo,0,0,+ velocidadCubo# * 2.5
	End If


	If KeyDown(20)
		If activaTexto = 0 Then
			activaTexto = 1
		Else
			activaTexto = 0
		End If
	End If


	If KeyDown(19) 	; Si pulsas la R, manda al cubo de vuelta 
					;a las coordenadas 0,0,0 y reinicia la velocidad
		posXCubo# = 0
		posYCubo# = 0 
		posZCubo# = 0
		velocidadCubo# = 0.5
		camMode = 0
	End If

	If rotXCubo# > 360
		rotXCubo# = 0
	End If
	If rotYCubo# > 360
		rotYCubo# = 0 ; Esto no es más que un poco de estética para las variables 
		;de debug que se muestran en pantalla, si una variable de rotación
		; pasa de 360, la ponemos a 0, que al final es lo mismo
	End If
	If rotZCubo# > 360
		rotZCubo# = 0
	End If


	If KeyDown(57) And delayBalas = 0 And cantidadBalas < 3 
		; Pulsamos el espacio, el código se ejecutará si han pasado 25 milisegundos
		;desde la última disparada y la cantidad de balas en movimiento es menor a 3
	
	
		
		;Actualizaremos el array de las balas
		balasJugador(cantidadBalas+1,1) = EntityX(cubo)
		balasJugador(cantidadBalas+1,2) = EntityY(cubo)
		balasJugador(cantidadBalas+1,3) = EntityZ(cubo)
		balasJugador(cantidadBalas+1,4) = 1
		balasJugador(cantidadBalas+1,5) = 1
		delayBalas = 25

	End If


	If KeyDown(25) ; Al pulsar la P cambiamos el modo de cámara al lateral
		camMode = 1
	End If


	; Una vez se han terminado las comprobaciones de controles, actualizamos las variables de posicionamiento y rotación
	; de las entidades

	;PositionEntity cubo,posXCubo#, posYCubo#, posZCubo#
	;RotateEntity cubo,rotXCubo#, -rotYCubo#, rotZCubo#
	;RotateEntity cubo,-camX#, -camY#, -camZ#
	EntityTexture cubo,texturaCubo
	
	If camMode = 0 ; Si el modo de cámara es 0, vamos a poner la cámara detrás del cubo
		;PositionEntity camara, posXCubo# , posYCubo# + 1, posZCubo# - 3
		;RotateEntity camara, -camX#, -camY#, -camZ#
		;RotateEntity camara,-rotXCubo#, -rotYCubo#, -rotZCubo#
	End If

	If camMode = 1 ;Si es uno, la ponemos en el lateral
		PositionEntity camara,16.5,0.0,11.0

		
		RotateEntity camara,0.0,90.5,0.0
	End If



	If delayBalas > 0 	; La variable del delay de las balas se irá reduciendo automáticamente de forma constante,
						; por tanto tendremos que esperar 25 milisegundos para volver a disparar
		delayBalas = delayBalas -1
	End If
	;ACtualizar balas
	For i=1 To 3
		PositionEntity balasJugadorModelo(i), balasJugador(i,1), balasJugador(i,2), balasJugador(i,3)
		If balasJugador(i,5) = 1
			balasJugador(i,3) = balasJugador(i,3) + balasJugador(i,4)
		End If
		If balasJugador(i,3) > 100 ; Si la posición Z de la bala es mayor de 100, no se puede mover
			balasJugador(i,5) = 0
			
			For z=1 To 2
				balasJugador(i,z)=9999
			Next
			balasJugador(i,3) = 0
			

		End If
	Next


	;Actualizar peces
	For i=1 To maxFish
		Local box_range# = 1 + pescaos(i,7)
		PositionEntity pescaosEnt(i,1), pescaos(i,1), pescaos(i,2), pescaos(i,3)
		pescaos(i,1) = pescaos(i,1) + pescaos(i,4)
		If pescaos(i,1) > 32

			;ScaleEntity pescaosEnt(i,1),0,0,0
			;FreeEntity pescaosEnt(i,1)
			;pescaosEnt(i,1) = CopyEntity(datosPescados(pescaos(i,5),1))
			;pescaosEnt(i,2) = datosPescados(pescaos(i,5),2)
			;EntityTexture pescaosEnt(i,1),pescaosEnt(i,2)
			;RotateEntity pescaosEnt(i,1),0,90,0


			;pescaos(i,1) = Rnd(datos2Pescados(pescaos(i,5),1),datos2Pescados(pescaos(i,5),2))
			;pescaos(i,2) = Rnd(datos2Pescados(pescaos(i,5),3),datos2Pescados(pescaos(i,5),4))
			;pescaos(i,3) = Rnd(datos2Pescados(pescaos(i,5),5),datos2Pescados(pescaos(i,5),6))
			;pescaos(i,4) = Rnd(datos2Pescados(pescaos(i,5),7),datos2Pescados(pescaos(i,5),8))
			;pescaos(i,5) = Rand(1,2)
			spawnFish(i,1)
			


		End If
		For ia=1 To 3 ; Colisión con balas
			Local zColM# = pescaos(i,3) + box_range * (datos2Pescados(pescaos(i,5),14))
			Local zColN# = pescaos(i,3) - box_range * (datos2Pescados(pescaos(i,5),14))
			Local yColM# = pescaos(i,2) + box_range * (datos2Pescados(pescaos(i,5),13))
			Local yColN# = pescaos(i,2) - box_range * (datos2Pescados(pescaos(i,5),13))
			Local xColM# = pescaos(i,1) + box_range * (datos2Pescados(pescaos(i,5),12))
			Local xColN# = pescaos(i,1) - box_range * (datos2Pescados(pescaos(i,5),12))
			colisions(0) = zColM
			colisions(1) = zColN
			colisions(2) = yColM
			colisions(3) = yColN
			colisions(4) = xColM
			colisions(5) = xColN
			If balasJugador(ia,3) < (zColM) 
				If balasJugador(ia,3) > (zColN) 
				
					If balasJugador(ia,2) < (yColM) 
						If balasJugador(ia,2) > (yColN) 
					
							If balasJugador(ia,1) < (xColM) 
								If balasJugador(ia,1) > (xColN)
									spawnFish(i,1)
									balasJugador(ia,3) = 9999
								End If
							End If
						End If
					End If
				End If
			End If
		Next
	Next






	cantidadBalas = balasJugador(1,5) + balasJugador(2,5) + balasJugador(3,5)
	

	Collisions type_bala,type_fish,3,1


	If EntityZ(cubo) > ((datosMapa(currentMapChunk,7) + datosMapa(currentMapChunk,4)) - 30) Then

		currentMapChunk = currentMapChunk + 1
		PositionEntity habitacion,datosMapa(currentMapChunk,5),datosMapa(currentMapChunk,6),datosMapa(currentMapChunk,7)
			


	End If


	If EntityZ(cubo) < ((datosMapa(currentMapChunk,7) - datosMapa(currentMapChunk,4)) + 30) Then

		currentMapChunk = currentMapChunk - 1
		PositionEntity habitacion,datosMapa(currentMapChunk,5),datosMapa(currentMapChunk,6),datosMapa(currentMapChunk,7)
			


	End If


End Function













; FUNCIONES DEL ARREGLO DE FILTRADO DE TEXTURAS

Function InitDX7Hack()
	DX7DBF_SetSystemProperties( SystemProperty("Direct3D7"), SystemProperty("Direct3DDevice7"), SystemProperty("DirectDraw7"), SystemProperty("AppHWND"), SystemProperty("AppHINSTANCE") )
End Function

Function DisableTextureFilters()
	For Level = 0 To 7
		DX7DBF_SetTextureStageState( Level, D3DTSS_MAGFILTER, D3DTEXF_POINT )
		DX7DBF_SetTextureStageState( Level, D3DTSS_MINFILTER, D3DTEXF_POINT )
		DX7DBF_SetTextureStageState( Level, D3DTSS_MIPFILTER, D3DTEXF_POINT  )
	Next
End Function

; FIN DE FUNCIONES DEL ARREGLO DE FILTRADO DE TEXTURAS


Function dibujaTexto(resWidth#,resHeight#, distancia, color1, color2, color3, fps, cubo)

	Color color1,color2,color3
	Text resWidth#*4/100+ distancia,resHeight#*4/100 + distancia,"Resolución horizontal:"+resWidth#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*8/100+ distancia,"Resolución vertical:"+resHeight#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*12/100+ distancia,"Posición cubo:"+ EntityX(cubo) + "," + EntityY(cubo) + "," + EntityZ(cubo)
	Text resWidth#*4/100+ distancia,resHeight#*16/100+ distancia,"Rotación cubo:"+ EntityPitch(cubo) + "," + EntityYaw(cubo) + "," + EntityRoll(cubo)
	Text resWidth#*4/100+ distancia,resHeight#*20/100+ distancia,"Velocidad cubo:"+ velocidadCubo#
	Text resWidth#*4/100+ distancia,resHeight#*24/100+ distancia,"Coordenadas del ratón:"+ MouseX() + "," + MouseY() + "," + MouseZ() 
	Text resWidth#*4/100+ distancia,resHeight#*28/100+ distancia,"Cantidad de balas disparadas:"+ cantidadBalas 
	Text resWidth#*4/100+ distancia,resHeight#*32/100+ distancia,"Delay balas:" + delayBalas
	For i=1 To 3
		Text resWidth#*4/100+ distancia + Len("Array balas:") + 50*i,resHeight#*36/100+ distancia, i
		For z=1 To 5
			Text resWidth#*4/100+ distancia + Len("Array balas:") + 50*i,resHeight#*36/100+ distancia + 32*z, balasJugador(i,z)
		Next
	Next
	Text resWidth#*50/100+ distancia,resHeight#*4/100+ distancia,"Cámara lateral = Tecla P"
	Text resWidth#*50/100+ distancia,resHeight#*8/100+ distancia,"WASD para rotar el cubo"
	Text resWidth#*50/100+ distancia,resHeight#*12/100+ distancia,"Espacio para disparar"
	Text resWidth#*50/100+ distancia,resHeight#*16/100+ distancia,"Current map chunk: " + currentMapChunk,0,0
	Text resWidth#*50/100+ distancia,resHeight#*20/100+ distancia,"FPS: " + fps,0,0
	For colText=0 To 5
		Text resWidth#*30/100+ distancia,resHeight#*(24+(4*colText))/100+ distancia,"Cols: " + colisions(colText),0,0
	Next


	Text resWidth#*4/100+ distancia,resHeight#*84/100+ distancia,"Usa las flechas de dirección para mover el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*88/100+ distancia,"Usa las teclas Q y E para subir y bajar el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*92/100+ distancia,"Usa las teclas + y - del teclado numérico para cambiar la velocidad.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*96/100+ distancia,"Pulsa la T para activar/desactivar este texto.",0,0

	;For i=1 To maxFish
	;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia, i
	;	For z=1 To 5
	;		Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 24*z, pescaos(i,z)
	;	Next
	;	For z=6 To 16
	;		
	;			Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 24*z, (datos2Pescados(pescaos(i,5),z-5))
	;		
	;	Next
		;z=6
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),1))
		;z=7
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),2))
		;z=8
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),3))
		;z=9
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),4))
		;z=10
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),5))
		;z=11
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),6))
		;z=12
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),7))
		;z=13
		;	Text resWidth#*50/100+ distancia + Len("Array peces:") + 80*i,resHeight#*36/100+ distancia + 32*z, (datos2Pescados(pescaos(i,5),8))
	;Next


	
End Function






initDX7Hack() ; Estas son las dos funciones que llamamos para que se arregle lo del filtrado de texturas que no me gusta
DisableTextureFilters()





While running = 1 ; Este es el bucle del juego, está corriendo constantemente

	
	

	If (MilliSecs() - logicTimer > 5)
		If (MilliSecs() - fpsTimer > 1000)
			fpsTimer = MilliSecs()
			fps = fpsTicks
			fpsTicks = 0
		Else
			fpsTicks = fpsTicks + 1
		EndIf
		Cls ; Limpiamos la pantalla
		RenderWorld ; Renderizamos la escena
		UpdateWorld ;Necesario para colisiones
		logicTimer = MilliSecs()
		logicaJuego(cubo, luz, texturaCubo, camara, habitacion) ; Llamamos a la función de la lógica para que se ejecute
		If activaTexto = 1 Then
			dibujatexto(resWidth#,resHeight#, 0, 0, 0, 0, fps,cubo) ; Dibujamos la sombra del texto
			dibujatexto(resWidth#,resHeight#, -2, 255,255,255, fps,cubo) ; Dibujamos el texto
		End If

		Flip ; Mandamos todo lo que se ha dibujado en memoria a la pantalla
	EndIf
	

	

; Y el bucle se repite constantemente


Wend