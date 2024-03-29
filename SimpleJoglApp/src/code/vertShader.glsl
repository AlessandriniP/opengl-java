#version 410

layout (location=0) in vec3 position;

uniform mat4 mv_matrix;
uniform mat4 proj_matrix;

out vec4 varyingColor;

void main(void)
{
	gl_Position = proj_matrix * mv_matrix * vec4(position,1.0);
	varyingColor = vec4(position, 1.0) * 0.5 + vec4(0.5, 0.5, 0.5, 0.5);
}

/**
 * UTILITY FUCTIONS
 * ----------------
 */

// utility function to build a translation matrix
mat4 buildTranslate(float x, float y, float z)
{
	mat4 trans = mat4(1.0, 0.0, 0.0, 0.0,
	                  0.0, 1.0, 0.0, 0.0,
				      0.0, 0.0, 1.0, 0.0,
				      x,   y,   z,   1.0);
	return trans;
}

// utility function to build a rotation matrix around the X axis
mat4 buildRotateX(float rad)
{
	mat4 xrot = mat4(1.0, 0.0,      0.0,       0.0,
			         0.0, cos(rad), -sin(rad), 0.0,
	                 0.0, sin(rad), cos(rad),  0.0,
	                 0.0, 0.0,      0.0,       1.0);
	return xrot;
}

// utility function to build a rotation matrix around the Y axis
mat4 buildRotateY(float rad)
{
	mat4 yrot = mat4(cos(rad),  0.0, sin(rad), 0.0,
					 0.0,       1.0, 0.0,      0.0,
	                 -sin(rad), 0.0, cos(rad), 0.0,
	                 0.0,       0.0, 0.0,      1.0);
	return yrot;
}

// utility function to build a rotation matrix around the Z axis
mat4 buildRotateZ(float rad)
{
	mat4 zrot = mat4(cos(rad), -sin(rad), 0.0, 0.0,
					 sin(rad), cos(rad),  0.0, 0.0,
	                 0.0,      0.0,       1.0, 0.0,
	                 0.0,      0.0,       0.0, 1.0);
	return zrot;
}

