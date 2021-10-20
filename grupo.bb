
Local resWidth#=Float 640 ; Resolución horizontal
Local resHeight#=Float 480 ; Resolución vertical
Local resCd=32 ; Profundidad de color
Local resWindow=0 ; Modo ventana: 0, pantalla completa 1
Local running=1 ; ¿Está el programa corriendo?






AppTitle "Trabajo en grupo - Eme, Juan y JD"
Graphics3D resWidth#, resHeight#, resCd, resWindow
SeedRnd(MilliSecs()) 
SetBuffer BackBuffer()

Local cubo=CreateCube()
Local camara=CreateCamera()
Local habitacion=CreateCube()
ScaleEntity habitacion,20.0,10.0,20.0
PositionEntity habitacion,0,0,5
FlipMesh habitacion
CameraViewport camara,0,0,resWidth#,resHeight#
luz=CreateLight(1)

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


Function logicaJuego(cubo, luz, texturaCubo, camara)
	
	;ClearTextureFilters() 

	;TextureFilter "",1
	


	If rotando = 1
		rotXCubo# = rotXCubo# + velocidadCubo#
		rotYCubo# = rotYCubo# + velocidadCubo#
		rotZCubo# = rotZCubo# + velocidadCubo#
	End If

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


	If rotXCubo# > 360
		rotXCubo# = 0
	End If
	If rotYCubo# > 360
		rotYCubo# = 0
	End If
	If rotZCubo# > 360
		rotZCubo# = 0
	End If

	PositionEntity cubo,posXCubo#, posYCubo#, posZCubo#
	;PositionEntity luz,posXCubo# + 5, posYCubo# + 5, posZCubo# + 5
	RotateEntity cubo,rotXCubo#, rotYCubo#, rotZCubo#
	EntityTexture cubo,texturaCubo
	PositionEntity camara, posXCubo# + 0.25, posYCubo# + 1, posZCubo# - 5
	RotateEntity camara, camX#, camY#, camZ#

	

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


	Text resWidth#*4/100+ distancia,resHeight#*84/100+ distancia,"Usa las flechas de dirección para mover el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*88/100+ distancia,"Usa las teclas Q y E para subir y bajar el cubo.",0,0
	Text resWidth#*4/100+ distancia,resHeight#*92/100+ distancia,"Usa las teclas + y - del teclado numérico para cambiar la velocidad.",0,0
End Function






initDX7Hack()
DisableTextureFilters()

EntityTexture habitacion,texturaSuelo

While running = 1

	Cls
	RenderWorld
	logicaJuego(cubo, luz, texturaCubo, camara)
	dibujatexto(resWidth#,resHeight#, 0, 0, 0, 0)
	dibujatexto(resWidth#,resHeight#, -2, 255,255,255)
	Flip




Wend