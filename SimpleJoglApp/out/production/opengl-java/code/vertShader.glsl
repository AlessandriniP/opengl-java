#version 430

uniform float inc;

// builds and returns a matrix that performs a rotation around the Z axis
mat4 buildRotateZ(float rad)
{
	mat4 zrot = mat4(cos(rad), -sin(rad), 0.0, 0.0,
					 sin(rad), cos(rad),  0.0, 0.0,
					 0.0,      0.0,       1.0, 0.0,
					 0.0,      0.0,       0.0, 1.0);
	return zrot;
}

void main(void)
{
	if (gl_VertexID == 0) gl_Position = buildRotateZ(1.0)*vec4(0.25,-0.25, 0.0, 1.0);
	else if (gl_VertexID == 1) gl_Position = buildRotateZ(1.0)*vec4(-0.25,-0.25, 0.0, 1.0);
	else gl_Position = buildRotateZ(1.0)*vec4(0.25, 0.25, 0.0, 1.0);
}
