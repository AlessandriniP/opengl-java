package code;

import java.nio.*;
import javax.swing.*;
import java.lang.Math;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLContext;

import com.jogamp.opengl.util.Animator;
import org.joml.*;

public class Code extends JFrame implements GLEventListener
{
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];
    private int vbo[] = new int[2];
    private float cameraX, cameraY, cameraZ;
    private float cubeLocX, cubeLocY, cubeLocZ;
    private float pyrLocX, pyrLocY, pyrLocZ;

    // allocate variables for display() function
    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);  // buffer for transfering matrix to uniform
    private Matrix4f pMat = new Matrix4f();  // perspective matrix
    private Matrix4f vMat = new Matrix4f();  // view matrix
    private Matrix4f mMat = new Matrix4f();  // model matrix
    private Matrix4f mvMat = new Matrix4f(); // model-view matrix
    private int mvLoc, projLoc;
    private float aspect;

    public Code()
    {
        setTitle("Computergrafik - Übungen");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        this.add(myCanvas);
        this.setVisible(true);

        // optional animator
        //Animator animator = new Animator(myCanvas);
        //animator.start();
    }

    public void display(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glClear(GL_COLOR_BUFFER_BIT);

        gl.glUseProgram(renderingProgram);

        mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        projLoc = gl.glGetUniformLocation(renderingProgram, "proj_matrix");

        aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);

        vMat.translation(-cameraX, -cameraY, -cameraZ);

        // draw the cube (use buffer #0)
        mMat.translation(cubeLocX, cubeLocY, cubeLocZ);

        mvMat.identity();
        mvMat.mul(vMat);
        mvMat.mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(projLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 36);

        // draw the pyramid (use buffer #1)
        mMat.translation(pyrLocX, pyrLocY, pyrLocZ);

        mvMat.identity();
        mvMat.mul(vMat);
        mvMat.mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(projLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 18);
    }

    public void init(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        renderingProgram = Utils.createShaderProgram("SimpleJoglApp/src/code/vertShader.glsl", "SimpleJoglApp/src/code/fragShader.glsl");
        setupVertices();
        // setup the position of the objects
        cameraX = 0.0f; cameraY = 0.0f; cameraZ = 8.0f;
        cubeLocX = 0.0f; cubeLocY = -2.0f; cubeLocZ = 0.0f;
        pyrLocX = 2.0f; pyrLocY = 2.0f; pyrLocZ = 0.0f;
    }

    private void setupVertices()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        float[] cubePositions =
                {
                        -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f, 1.0f,  1.0f, -1.0f, -1.0f,  1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f, 1.0f,  1.0f, -1.0f,
                        1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f, 1.0f,  1.0f, -1.0f,
                        1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
                        -1.0f, -1.0f,  1.0f, -1.0f,  1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
                        -1.0f, -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f,  1.0f,
                        -1.0f, -1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f,  1.0f,
                        -1.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f,
                        -1.0f,  1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,  1.0f,  1.0f,
                        1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f, -1.0f
                };

        float[] pyramidPositions =
                {
                        -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, // front face
                        1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, // right face
                        1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, // back face
                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, // left face
                        -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, // base - left front
                        1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f // base - right back
                };

        // vao
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);

        // vbo for the cube
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer cubeBuf = Buffers.newDirectFloatBuffer(cubePositions);
        gl.glBufferData(GL_ARRAY_BUFFER, cubeBuf.limit()*4, cubeBuf, GL_STATIC_DRAW);

        // vbo for the pyramid
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        FloatBuffer pyrBuf = Buffers.newDirectFloatBuffer(pyramidPositions);
        gl.glBufferData(GL_ARRAY_BUFFER, pyrBuf.limit()*4, pyrBuf, GL_STATIC_DRAW);
    }

    public static void main(String[] args) { new Code(); }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    public void dispose(GLAutoDrawable drawable) {}
}