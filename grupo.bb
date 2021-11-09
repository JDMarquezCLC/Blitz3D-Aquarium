
; ARREGLO DEL FILTRADO DE TEXTURAS

Const D3DTSS_MAGFILTER = 16
Const D3DTSS_MINFILTER = 17
Const D3DTSS_MIPFILTER = 18
Const D3DTEXF_NONE = 0 
Const D3DTEXF_POINT = 1
Const D3DTEXF_LINEAR = 2

; FIN DE LAS VARIABLES DEL ARREGLO DE FILTRADO DE TEXTURAS


Local resWidth#=Float 640 ; Resoluci�n horizontal
Local resHeight#=Float 480 ; Resoluci�n vertical
Local resCd=32 ; Profundidad de color
Local resWindow=0 ; Modo ventana: 0, pantalla completa 1

AppTitle "Trabajo en grupo - Eme, Juan y JD"
Graphics3D resWidth#, resHeight#, resCd, resWindow 
SeedRnd(MilliSecs())
SetBuffer BackBuffer() 

Local running=1 ; �Est� el programa corriendo?
Local cubo=LoadMesh("pescao.3ds") 
Local camara=CreateCamera()
Local habitacion=CreateCube()
Local luz=CreateLight(1)
Local texturaCubo = LoadTexture("pescao.png")
Local texturaSuelo = LoadTexture("pared.png")

; Creaci�n de variables globales, que pueden ser accedidas desde cualquier sitio

; Posicionamiento y rotaci�n del cubo
Global posXCubo# = Float 0
Global posYCubo# = Float 0
Global posZCubo# = Float 5 
Global rotXCubo# = Float 0
Global rotYCubo# = Float 0
Global rotZCubo# = Float 0
Global rotando = 0
Global velocidadCubo# = 0.5
; Posicionamiento de la c�mara
Global camX# = 0
Global camY# = 0 
Global camZ# = 0
Global camMode = 0 
Global cantidadBalas = 0
Global delayBalas = 0










; Manipulaci�n de entidades inicial

ScaleEntity habitacion,20.0,10.0,20.0
ScaleEntity cubo,0.75,0.75,0.75
PositionEntity habitacion,0,0,5
FlipMesh habitacion 
CameraViewport camara,0,0,resWidth#,resHeight# 	
EntityTexture habitacion,texturaSuelo

;Creaci�n de las balas
Dim balasJugador (3,6) ; Array de las balas del jugador
For i=1 To 3 
	balasJugador(i,6)=CreateCube() ;Creaci�n de bala
	For z=1 To 2
		balasJugador(i,z)=9999 ; Posici�n inicial
	Next
	ScaleEntity balasJugador(i,6),0.25,0.25,0.25
Next

; Podremos almacenar hasta 3 balas, y cada una tendr� 3 coordenadas de posici�n y una velocidad, y un �ltimo valor para saber si debe moverse o no, el �ltimo de verdad es para el modelo


Dim pescaos# (10,6) ; Array de las balas del jugador
;posX,posY,posZ,velocidad,modelo, textura
For i=1 To 10
	pescaos(i,1) = Rnd(-32,-48)
	pescaos(i,2) = Rnd(-0.5,8)
	pescaos(i,3) = Rnd(-12,22)
	pescaos(i,4) = Float Rnd(0.1,0.5)
	pescaos(i,5) = LoadMesh("pescao.3ds") 
	pescaos(i,6) = LoadTexture("pescao.png")
	EntityTexture pescaos(i,5),pescaos(i,6)
	RotateEntity pescaos(i,5),0,90,0
Next










Function logicaJuego(cubo, luz, texturaCubo, camara) 	; Aqu� funcionar� toda la l�gica del juego, llamamos esta funci�n en el bucle y se repite constantemente
														;Le daremos como argumentos las entidades LOCALES del cubo, la luz y la c�mara, y la textura del cubo
	

	Local random#
	
	; CONTROLES DEL CUBO
	If KeyDown(200)
		posZCubo# = posZCubo# + velocidadCubo#
	End If

	If KeyDown(208)
		posZCubo# = posZCubo# - velocidadCubo#
	End If

	If KeyDown(203)
		posXCubo# = posXCubo# - velocidadCubo#
	End If

	If KeyDown(205)
		posXCubo# = posXCubo# + velocidadCubo#
	End If

	If KeyDown(78)
		velocidadCubo# = velocidadCubo# + 0.01
	End If

	If KeyDown(74)
		velocidadCubo# = velocidadCubo# - 0.01
	End If

	If KeyDown(16)
		posYCubo# = posYCubo# + velocidadCubo#
	End If

	If KeyDown(18)
		posYCubo# = posYCubo# - velocidadCubo#
	End If

	If KeyDown(30)
		rotYCubo# = rotYCubo# - velocidadCubo#
	End If
	If KeyDown(32)
		rotYCubo# = rotYCubo# + velocidadCubo#
	End If

	If KeyDown(17)
		rotXCubo# = rotXCubo# + velocidadCubo#
	End If

	If KeyDown(31)
		rotXCubo# = rotXCubo# - velocidadCubo#
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
		rotYCubo# = 0 ; Esto no es m�s que un poco de est�tica para las variables 
		;de debug que se muestran en pantalla, si una variable de rotaci�n
		; pasa de 360, la ponemos a 0, que al final es lo mismo
	End If
	If rotZCubo# > 360
		rotZCubo# = 0
	End If


	If KeyDown(57) And delayBalas = 0 And cantidadBalas < 3 
		; Pulsamos el espacio, el c�digo se ejecutar� si han pasado 25 milisegundos
		;desde la �ltima disparada y la cantidad de balas en movimiento es menor a 3
	
	
		
		;Actualizaremos el array de las balas
		balasJugador(cantidadBalas+1,1) = posXCubo#
		balasJugador(cantidadBalas+1,2) = posYCubo#
		balasJugador(cantidadBalas+1,3) = posZCubo#
		balasJugador(cantidadBalas+1,4) = 1
		balasJugador(cantidadBalas+1,5) = 1
		delayBalas = 25

	End If


	If KeyDown(25) ; Al pulsar la P cambiamos el modo de c�mara al lateral
		camMode = 1
	End If


	; Una vez se han terminado las comprobaciones de controles, actualizamos las variables de posicionamiento y rotaci�n
	; de las entidades

	PositionEntity cubo,posXCubo#, posYCubo#, posZCubo#
	RotateEntity cubo,rotXCubo#, rotYCubo#, rotZCubo#
	EntityTexture cubo,texturaCubo
	
	If camMode = 0 ; Si el modo de c�mara es 0, vamos a poner la c�mara detr�s del cubo
		PositionEntity camara, posXCubo# + 0.25, posYCubo# + 2, posZCubo# - 5
		RotateEntity camara, camX#, camY#, camZ#
	End If

	If camMode = 1 ;Si es uno, la ponemos en el lateral
		PositionEntity camara,16.5,0.0,11.0
		RotateEntity camara,0.0,90.5,0.0
	End If



	If delayBalas > 0 	; La variable del delay de las balas se ir� reduciendo autom�ticamente de forma constante,
						; por tanto tendremos que esperar 25 milisegundos para volver a disparar
		delayBalas = delayBalas -1
	End If
	;ACtualizar balas
	For i=1 To 3
		PositionEntity balasJugador(i,6), balasJugador(i,1), balasJugador(i,2), balasJugador(i,3)
		If balasJugador(i,5) = 1
			balasJugador(i,3) = balasJugador(i,3) + balasJugador(i,4)
		End If
		If balasJugador(i,3) > 100 ; Si la posici�n Z de la bala es mayor de 100, no se puede mover
			balasJugador(i,5) = 0
			
			For z=1 To 2
				balasJugador(i,z)=9999
			Next
			balasJugador(i,3) = 0
			

		End If
	Next


	;Actualizar peces
	For i=1 To 10
		PositionEntity pescaos(i,5), pescaos(i,1), pescaos(i,2), pescaos(i,3)
		pescaos(i,1) = pescaos(i,1) + pescaos(i,4)
		If pescaos(i,1) > 32 ; Si la posici�n Z de la bala es mayor de 100, no se puede mover
			pescaos(i,1) = Rnd(-32,-48)
			pescaos(i,2) = Rnd(-0.5,8)
			pescaos(i,3) = Rnd(-12,22)
			pescaos(i,4) = Float Rnd(0.1,0.5)
		End If
	Next






	cantidadBalas = balasJugador(1,5) + balasJugador(2,5) + balasJugador(3,5)
	

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


Function dibujaTexto(resWidth#,resHeight#, distancia, color1, color2, color3)

	Color color1,color2,color3
	Text resWidth#*4/100+ distancia,resHeight#*4/100 + distancia,"Resoluci�n horizontal:"+resWidth#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*8/100+ distancia,"Resoluci�n vertical:"+resHeight#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*12/100+ distancia,"Posici�n cubo:"+ posXCubo# + "," + posYCubo# + "," + posZCubo#
	Text resWidth#*4/100+ distancia,resHeight#*16/100+ distancia,"Rotaci�n cubo:"+ rotXCubo# + "," + rotYCubo# + "," + rotZCubo#
	Text resWidth#*4/100+ distancia,resHeight#*20/100+ distancia,"Velocidad cubo:"+ velocidadCubo#
	Text resWidth#*4/100+ distancia,resHeight#*24/100+ distancia,"Coordenadas del rat�n:"+ MouseX() + "," + MouseY() + "," + MouseZ() 
	Text resWidth#*4/100+ distancia,resHeight#*28/100+ distancia,"Cantidad de balas disparadas:"+ cantidadBalas 
	Text resWidth#*4/100+ distancia,resHeight#*32/100+ distancia,"Delay balas:" + delayBalas
	For i=1 To 3
		Text resWidth#*4/100+ distancia + Len("Array balas:") + 32*i,resHeight#*36/100+ distancia, i
		For z=1 To 5
			Text resWidth#*4/100+ distancia + Len("Array balas:") + 32*i,resHeight#*36/100+ distancia + 32*z, balasJugador(i,z)
		Next
	Next
	Text resWidth#*50/100+ distancia,resHeight#*4/100+ distancia,"C�mara lateral = Tecla P"
	Text resWidth#*50/100+ distancia,resHeight#*8/100+ distancia,"WASD para rotar el cubo"
	Text resWidth#*50/100+ distancia,resHeight#*12/100+ distancia,"Espacio para disparar"


	Text resWidth#*4/100+ distancia,resHeight#*84/100+ distancia,"Usa las flechas de direcci�n para mover el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*88/100+ distancia,"Usa las teclas Q y E para subir y bajar el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*92/100+ distancia,"Usa las teclas + y - del teclado num�rico para cambiar la velocidad.",0,0
End Function






initDX7Hack() ; Estas son las dos funciones que llamamos para que se arregle lo del filtrado de texturas que no me gusta
DisableTextureFilters()





While running = 1 ; Este es el bucle del juego, est� corriendo constantemente

	Cls ; Limpiamos la pantalla
	RenderWorld ; Renderizamos la escena
	logicaJuego(cubo, luz, texturaCubo, camara) ; Llamamos a la funci�n de la l�gica para que se ejecute
	dibujatexto(resWidth#,resHeight#, 0, 0, 0, 0) ; Dibujamos la sombra del texto
	dibujatexto(resWidth#,resHeight#, -2, 255,255,255) ; Dibujamos el texto
	Flip ; Mandamos todo lo que se ha dibujado en memoria a la pantalla

; Y el bucle se repite constantemente


Wend