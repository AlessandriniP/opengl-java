#version 430

in vec4 varyingColor;

uniform mat4 v_matrix; // only the P abd V matrices are sent from the application
uniform mat4 proj_matrix;
uniform float timeFactor; // time factor for animation and placement of cubes

out vec4 color;

void main(void)
{
	color = varyingColor;
}