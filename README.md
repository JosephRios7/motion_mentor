# MotionMentor

MotionMentor es un sistema multiagente (SMA) diseñado para guiar a los usuarios en ejercicios físicos mediante el monitoreo por cámara, considerando los estilos de aprendizaje auditivo-visual e inteligencias múltiples corporal-kinestésica y visual-espacial.

## Objetivo

El objetivo de MotionMentor es proporcionar a los usuarios una experiencia de entrenamiento físico inteligente, guiada y personalizada utilizando tecnología avanzada. El sistema se enfoca en prevenir lesiones y mejorar el rendimiento del usuario al adaptar los ejercicios a sus necesidades individuales.

## Problema a Resolver

El sistema MotionMentor aborda la necesidad de una guía inteligente y personalizada para el entrenamiento físico, especialmente para personas principiantes o aquellas que buscan evitar lesiones durante el ejercicio. También puede servir como una herramienta útil para instructores al proporcionar retroalimentación inmediata y adaptar los ejercicios según el rendimiento del usuario.

## Agentes

### Agente de Visión (AV)

El Agente de Visión utiliza algoritmos de visión por computadora para analizar en tiempo real las imágenes de la cámara y reconocer la posición y movimiento del usuario durante el ejercicio. Esto permite al sistema realizar un seguimiento preciso del usuario y proporcionar retroalimentación específica sobre su rendimiento.

### Agente de Retroalimentación (AR)

El Agente de Retroalimentación proporciona retroalimentación inmediata basada en la información recopilada por el sistema de visión. Puede sugerir correcciones posturales, elogiar al usuario por realizar correctamente un ejercicio o adaptar el nivel de dificultad según su rendimiento. Esta retroalimentación personalizada ayuda al usuario a mejorar y optimizar su entrenamiento físico.

## Librerías Utilizadas

- JADE Framework
- OpenCV
- eSpeak (programa para la voz del programa)

## Algoritmo de RecomendadorEjercicios

El algoritmo del RecomendadorEjercicios utiliza el enfoque de K-Nearest Neighbors (KNN) para recomendar ejercicios basados en el tipo deseado y la dificultad deseada. El algoritmo calcula la distancia entre el ejercicio deseado y todos los demás ejercicios disponibles, utilizando medidas de distancia en las dimensiones del tipo de ejercicio y la dificultad. Luego, ordena los ejercicios por distancia ascendente y devuelve los k ejercicios más cercanos como recomendaciones.

## Contribución

¡Todas las contribuciones son bienvenidas! Si desea contribuir a MotionMentor, puede enviar solicitudes de extracción con sus mejoras, correcciones de errores o nuevas características. También puede informar problemas o sugerir ideas para mejorar el proyecto.
