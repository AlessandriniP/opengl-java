package main;

import javax.swing.*;
import static com.jogamp.opengl.GL4.*;

import com.jogamp.graph.geom.SVertex;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Code extends JFrame implements GLEventListener {

    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];

    public Code() {
        setTitle("Chapter2 - program1");
        setSize(600, 400);
        setLocation(200, 200);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        this.add(myCanvas);
        this.setVisible(true);
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(renderingProgram);
        gl.glPointSize(30.0f);
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        gl.glDrawArrays(GL_POINTS, 0, 1);
    }

    public void init(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        renderingProgram = createShaderProgram();
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
    }

    private int createShaderProgram() {
        GL4 gl = (GL4) GLContext.getCurrentGL();

        /**
        // arrays to collect GLSL compilation status values.
        // note: one-element arrays are used because the associated JOGL calls require arrays.
        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] linked = new int[1];
         */

        String vshaderSource[] = readShaderSource("out/production/SimpleJoglApp/main/vertShader.glsl");
        String fshaderSource[] = readShaderSource("out/production/SimpleJoglApp/main/fragShader.glsl");

        int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(vShader, 6, vshaderSource, null, 0); // 6 is the count of lines of source code
        gl.glCompileShader(vShader);

        int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(fShader, 11, fshaderSource, null, 0); // 11 is the count of lines of source code
        gl.glCompileShader(fShader);

        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);

        /**
        // catch errors while compiling shaders

        gl.glCompileShader(vShader);
        checkOpenGLError();
        gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled, 0);
        if (vertCompiled[0] != 1)
        {
            System.out.println("vertex compilation failed.");
            printShaderLog(vShader);
        }

        gl.glCompileShader(fShader);
        checkOpenGLError();
        gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled, 0);
        if (fragCompiled[0] != 1)
        {
            System.out.println("fragment compilation failed.");
            printShaderLog(fShader);
        }

        if ((vertCompiled[0] != 1) || (fragCompiled[0] != 1))
        {
            System.out.println("\nCompilation error; return-flags:");
            System.out.println(" vertCompiled = " + vertCompiled[0] + "; fragCompiled = " + fragCompiled[0]);
        }
        */

        int vfProgram = gl.glCreateProgram();
        gl.glAttachShader(vfProgram, vShader);
        gl.glAttachShader(vfProgram, fShader);
        gl.glLinkProgram(vfProgram);

        /**
        // catch errors while linking shaders
        gl.glLinkProgram(vfProgram);
        checkOpenGLError();
        gl.glGetProgramiv(vfProgram, GL_LINK_STATUS, linked, 0);
        if (linked[0] != 1)
        {
            System.out.println("linking failed.");
            printProgramLog(vfProgram);
        }
        */

        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);

        return vfProgram;
    }

    /**
    // 3 Modules to Catch GLSL Errors
    // 1
    private void printShaderLog(int shader)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWrittn = new int[1];
        byte[] log = null;

        // determine the length of the shader compilation log
        gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {
            log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, len[0], chWrittn, 0, log, 0);
            System.out.println("Shader Info Log: ");
            for (int i = 0; i < log.length; i++)
            {
                System.out.println((char) log[i]);
            }
        }
    }
    // 2
    void printProgramLog(int prog)
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] len = new int[1];
        int[] chWrittn = new int[1];
        byte[] log = null;

        // determine the length of the program linking log
        gl.glGetProgramiv(prog, GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {
            log = new byte[len[0]];
            gl.glGetShaderInfoLog(prog, len[0], chWrittn, 0, log, 0);
            System.out.println("Program Info Log: ");
            for (int i = 0; i < log.length; i++)
            {
                System.out.println((char) log[i]);
            }
        }
    }
    // 3
    boolean checkOpenGLError()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        boolean foundError = false;
        GLU glu = new GLU();
        int glErr = gl.glGetError();
        while(glErr != GL_NO_ERROR)
        {
            System.err.print("glError: " + glu.gluErrorString(glErr));
            foundError = true;
            glErr = gl.glGetError();
        }
        return foundError;
    }
    */

    private String[] readShaderSource(String filename)
    {
        Vector<String> lines = new Vector<>();
        Scanner sc;
        String[] program;
        try
        {
            sc = new Scanner(new File(filename));
            while (sc.hasNext())
            {
                lines.addElement(sc.nextLine());
            }
            program = new String[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                program[i] = (String) lines.elementAt(i) + "\n";
            }
        }
        catch (IOException e)
        {
            System.err.println("IOException reading file: " + e);
            return null;
        }
        return program;
    }

    public static void main(String[] args) {
        new Code();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    public void dispose(GLAutoDrawable drawable) {}
}
