
Local resWidth#=Float 640 ; Resolución horizontal
Local resHeight#=Float 480 ; Resolución vertical
Local resCd=32 ; Profundidad de color
Local resWindow=0 ; Modo ventana: 0, pantalla completa 1
Local running=1 ; ¿Está el programa corriendo?






AppTitle "Trabajo en grupo - Eme, Juan y JD"
Graphics3D resWidth#, resHeight#, resCd, resWindow ; Esta línea establece la pantalla del programa, los parámetros son la resolución, la profundidad de color y si es pantalla completa o una ventana
SeedRnd(MilliSecs()) ; Esto sirve para poner una nueva semilla para el generador de números aleatorios, que aún no he usado
SetBuffer BackBuffer() ; Esto sirve para decirle al programa que todas las operaciones de dibujado las haga en memoria, más tarde en el bucle se intercambiarán la memoria con la pantalla con el flip()
; Basicamente esto hay que hacerlo para que no haya cortes en la pantalla, es decir, primero dibujamos todo en memoria, y cuando ya tengamos el fotograma completo lo mandamos a la pantalla

Local cubo=CreateCube() 
Local camara=CreateCamera()
Local habitacion=CreateCube()
;Local bala=CreateCube()
ScaleEntity habitacion,20.0,10.0,20.0
;ScaleEntity bala,0.25,0.25,0.25 ; Objeto de proyectil
;PositionEntity bala,9999,9999,9999
PositionEntity habitacion,0,0,5
FlipMesh habitacion ; Con esto volteamos los normales de un modelo, cosa que significa que las caras de dentro serán consideradas las caras visibles
; Basicamente, no veremos las caras de fuera pero sí las de dentro
CameraViewport camara,0,0,resWidth#,resHeight#
luz=CreateLight(1)


;Type BALA ; Creación del objeto bala
;Field MODEL
;Field X ;Posición
;Field Y
;Field Z
;Field VEL ; Velocidad
;Field SX ; Escala
;Field SY
;Field SZ
;End Type


; Creamos una bala inicial

		



Global posXCubo# = Float 0
Global posYCubo# = Float 0
Global posZCubo# = Float 5
Global rotXCubo# = Float 0
Global rotYCubo# = Float 0
Global rotZCubo# = Float 0
Global rotando = 0
Global velocidadCubo# = 0.5
Global camX# = 0
Global camY# = 0
Global camZ# = 0
;Global balaX# = 9999
;Global balaY# = 9999
;Global balaZ# = 9999
Global disparado = 0 ; Con esta variable sabremos si el jugador ha disparado ya o no
; Aquí he usado variables globlales y no locales para que sea más fácil trabajar con las distintas entidades que he creado


Global camMode = 0 ; Comprobamos cuál es el modo de cámara actual, con el 0 la cámara se posiciona detrás del jugador, con el 1 se pone a un lado para ver las balas


Dim balasJugador (3,6) ; Array de las balas del jugador

Global cantidadBalas = 0 ; ¿Cuántas balas hemos disparado ya?

Global delayBalas = 0 ; El tiempo de espera desde que podemos disparar una bala hasta disparar otra

For i=1 To 3 
	balasJugador(i,6)=CreateCube()
	For z=1 To 2
		balasJugador(i,z)=9999
	Next
	ScaleEntity balasJugador(i,6),0.25,0.25,0.25
Next

; Podremos almacenar hasta 3 balas, y cada una tendrá 3 coordenadas de posición y una velocidad, y un último valor para saber si debe moverse o no, el último de verdad es para el modelo



; ARREGLO DEL FILTRADO DE TEXTURAS

Const D3DTSS_MAGFILTER = 16
Const D3DTSS_MINFILTER = 17
Const D3DTSS_MIPFILTER = 18
Const D3DTEXF_NONE = 0 
Const D3DTEXF_POINT = 1
Const D3DTEXF_LINEAR = 2

; FIN DE LAS VARIABLES DEL ARREGLO DE FILTRADO DE TEXTURAS

Local texturaCubo = LoadTexture("cubo.png")
Local texturaSuelo = LoadTexture("pared.png")

;TextureBlend texturaCubo,0


Function logicaJuego(cubo, luz, texturaCubo, camara) ; Aquí funcionará toda la lógica del juego, llamamos esta función en el bucle y se repite constantemente
	
	;ClearTextureFilters() 

	;TextureFilter "",1
	

	Local random#


	If rotando = 1 ; Si ponemos la variable de rotando a 1, se activará el modo en el que el cubo no para de dar vueltas
		SeedRnd (MilliSecs()) 
		random# = (Rand(5,10) / 100)

		rotXCubo# = rotXCubo# + random#
		rotYCubo# = rotYCubo# + random#
		rotZCubo# = rotZCubo# + random#
	End If
	
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


	If KeyDown(19) ; Si pulsas la R, manda al cubo de vuelta a las coordenadas 0,0,0 y reinicia la velocidad
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
		rotYCubo# = 0 ; Esto no es más que un poco de estética para las variables de debug que se muestran en pantalla, si una variable de rotación
		; pasa de 360, la ponemos a 0, que al final es lo mismo
	End If
	If rotZCubo# > 360
		rotZCubo# = 0
	End If


	If KeyDown(57) And delayBalas = 0 And cantidadBalas < 3 ; Pulsamos el espacio



		
		balasJugador(cantidadBalas+1,1) = posXCubo#
		balasJugador(cantidadBalas+1,2) = posYCubo#
		balasJugador(cantidadBalas+1,3) = posZCubo#
		balasJugador(cantidadBalas+1,4) = 1
		balasJugador(cantidadBalas+1,5) = 1
		delayBalas = 25





	End If

	;	balaJugador.BALA = New BALA
	;	balaJugador\MODEL = CreateCube()
	;	balaJugador\X = posXCubo# + 0.5
	;	balaJugador\Y = posYCubo# + 0.5
	;	balaJugador\Z = posZCubo# + 0.5
	;	balaJugador\VEL = 2
	;	balaJugador\SX = 0.25
	;	balaJugador\SY = 0.25
	;	balaJugador\SZ = 0.25


	

	;For balaJugador.BALA = Each BALA
	;	balaJugador\Z = balaJugador\Z + balaJugador\VEL
	;Next

	If KeyDown(25)
		camMode = 1
	End If

	PositionEntity cubo,posXCubo#, posYCubo#, posZCubo#
	;PositionEntity luz,posXCubo# + 5, posYCubo# + 5, posZCubo# + 5
	RotateEntity cubo,rotXCubo#, rotYCubo#, rotZCubo#
	EntityTexture cubo,texturaCubo
	If camMode = 0
		PositionEntity camara, posXCubo# + 0.25, posYCubo# + 1, posZCubo# - 5
		RotateEntity camara, camX#, camY#, camZ#
	End If

	If camMode = 1
		PositionEntity camara,16.5,0.0,11.0
		RotateEntity camara,0.0,90.5,0.0
	End If


	;PositionEntity balaJugador\MODEL.BALA,balaJugador\X, balaJugador\Y, balaJugador\Z
	If delayBalas > 0
		delayBalas = delayBalas -1
	End If

	For i=1 To 3
		PositionEntity balasJugador(i,6), balasJugador(i,1), balasJugador(i,2), balasJugador(i,3)
		If balasJugador(i,5) = 1
			balasJugador(i,3) = balasJugador(i,3) + balasJugador(i,4)
		End If
		If balasJugador(i,3) > 100
			balasJugador(i,5) = 0
			
			For z=1 To 2
				balasJugador(i,z)=9999
			Next
			balasJugador(i,3) = 0
			;cantidadBalas = cantidadBalas - 1

		End If
	Next

	;If balasJugador(1,3) + balasJugador(2,3) + balasJugador(3,3) = 0
	;	cantidadBalas = 0
	;End If
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
	Text resWidth#*4/100+ distancia,resHeight#*4/100 + distancia,"Resolución horizontal:"+resWidth#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*8/100+ distancia,"Resolución vertical:"+resHeight#,0,0
	Text resWidth#*4/100+ distancia,resHeight#*12/100+ distancia,"Posición cubo:"+ posXCubo# + "," + posYCubo# + "," + posZCubo#
	Text resWidth#*4/100+ distancia,resHeight#*16/100+ distancia,"Rotación cubo:"+ rotXCubo# + "," + rotYCubo# + "," + rotZCubo#
	Text resWidth#*4/100+ distancia,resHeight#*20/100+ distancia,"Velocidad cubo:"+ velocidadCubo#
	Text resWidth#*4/100+ distancia,resHeight#*24/100+ distancia,"Coordenadas del ratón:"+ MouseX() + "," + MouseY() + "," + MouseZ() 
	Text resWidth#*4/100+ distancia,resHeight#*28/100+ distancia,"Cantidad de balas disparadas:"+ cantidadBalas 
	Text resWidth#*4/100+ distancia,resHeight#*32/100+ distancia,"Delay balas:" + delayBalas
	For i=1 To 3
		Text resWidth#*4/100+ distancia + Len("Array balas:") + 32*i,resHeight#*36/100+ distancia, i
		For z=1 To 6
			Text resWidth#*4/100+ distancia + Len("Array balas:") + 32*i,resHeight#*36/100+ distancia + 32*z, balasJugador(i,z)
		Next
	Next
	Text resWidth#*50/100+ distancia,resHeight#*4/100+ distancia,"Cámara lateral = Tecla P"
	Text resWidth#*50/100+ distancia,resHeight#*8/100+ distancia,"WASD para rotar el cubo"
	Text resWidth#*50/100+ distancia,resHeight#*12/100+ distancia,"Espacio para disparar"


	Text resWidth#*4/100+ distancia,resHeight#*84/100+ distancia,"Usa las flechas de dirección para mover el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*88/100+ distancia,"Usa las teclas Q y E para subir y bajar el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*92/100+ distancia,"Usa las teclas + y - del teclado numérico para cambiar la velocidad.",0,0
End Function






initDX7Hack() ; Estas son las dos funciones que llamamos para que se arregle lo del filtrado de texturas que no me gusta
DisableTextureFilters()

EntityTexture habitacion,texturaSuelo



		;balaJugador.BALA = New BALA
		;balaJugador\MODEL = CreateCube()
		;balaJugador\X = posXCubo# + 0.5
		;balaJugador\Y = posYCubo# + 0.5
		;balaJugador\Z = posZCubo# + 0.5
		;balaJugador\VEL = 2
		;balaJugador\SX = 0.25
		;balaJugador\SY = 0.25
		;balaJugador\SZ = 0.25


While running = 1 ; Este es el bucle del juego, está corriendo constantemente

	Cls ; Limpiamos la pantalla
	RenderWorld ; Renderizamos la escena
	logicaJuego(cubo, luz, texturaCubo, camara) ; Llamamos a la función de la lógica para que se ejecute
	dibujatexto(resWidth#,resHeight#, 0, 0, 0, 0) ; Dibujamos la sombra del texto
	dibujatexto(resWidth#,resHeight#, -2, 255,255,255) ; Dibujamos el texto
	Flip ; Mandamos todo lo que se ha dibujado en memoria a la pantalla

; Y el bucle se repite constantemente


Wend
