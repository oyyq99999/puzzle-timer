package com.puzzletimer;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.KeyStroke;

import com.puzzletimer.geometry.Plane;
import com.puzzletimer.graphics.HSLColor;
import com.puzzletimer.graphics.Mesh;
import com.puzzletimer.linearalgebra.Matrix33;
import com.puzzletimer.linearalgebra.Vector3;
import com.puzzletimer.scrambles.CubeMove;
import com.puzzletimer.scrambles.Move;
import com.puzzletimer.scrambles.RubiksCubeRandomScrambler;
import com.puzzletimer.scrambles.Scramble;
import com.puzzletimer.scrambles.Scrambler;

public class RubiksCube implements Puzzle {
    private Scrambler scrambler;

    public RubiksCube() {
        scrambler = new RubiksCubeRandomScrambler(20);
    }

    @Override
    public String getName() {
        return "Rubik's Cube";
    }

    @Override
    public int getMnemonic() {
        return KeyEvent.VK_R;
    }

    @Override
    public KeyStroke getAccelerator() {
        return KeyStroke.getKeyStroke('3', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }

    @Override
    public boolean isDefaultPuzzle()
    {
        return true;
    }

   @Override
    public Scrambler getScrambler()
    {
        return scrambler;
    }

    private class Twist {
        public Plane plane;
        public double angle;

        public Twist(Plane plane, double angle) {
            this.plane = plane;
            this.angle = angle;
        }
    }

    @Override
    public Mesh getMesh(Scramble s)
    {
        HSLColor[] colors = {
            new HSLColor( 20, 100,  50), // L - orange
            new HSLColor(235, 100,  30), // B - blue
            new HSLColor( 55, 100,  50), // D - yellow
            new HSLColor(  0,  85,  45), // R - red
            new HSLColor(120, 100,  30), // F - green
            new HSLColor(  0,   0, 100), // U - white
        };

        Mesh mesh = Mesh.cube(colors);

        Plane planeL = new Plane(new Vector3(-0.166, 0, 0), new Vector3(-1, 0, 0));
        Plane planeR = new Plane(new Vector3( 0.166, 0, 0), new Vector3( 1, 0, 0));
        Plane planeD = new Plane(new Vector3(0, -0.166, 0), new Vector3(0, -1, 0));
        Plane planeU = new Plane(new Vector3(0,  0.166, 0), new Vector3(0,  1, 0));
        Plane planeF = new Plane(new Vector3(0, 0, -0.166), new Vector3(0, 0, -1));
        Plane planeB = new Plane(new Vector3(0, 0,  0.166), new Vector3(0, 0,  1));

        mesh = mesh
            .cut(planeL, 0)
            .cut(planeR, 0)
            .cut(planeD, 0)
            .cut(planeU, 0)
            .cut(planeF, 0)
            .cut(planeB, 0)
            .shortenFaces(0.03)
            .softenFaces(0.015)
            .softenFaces(0.005);

        HashMap<Move, Twist> twists = new HashMap<Move, Twist>();
        twists.put(CubeMove.L,  new Twist(planeL,  Math.PI / 2));
        twists.put(CubeMove.L2, new Twist(planeL,  Math.PI));
        twists.put(CubeMove.L3, new Twist(planeL, -Math.PI / 2));
        twists.put(CubeMove.R,  new Twist(planeR,  Math.PI / 2));
        twists.put(CubeMove.R2, new Twist(planeR,  Math.PI));
        twists.put(CubeMove.R3, new Twist(planeR, -Math.PI / 2));
        twists.put(CubeMove.D,  new Twist(planeD,  Math.PI / 2));
        twists.put(CubeMove.D2, new Twist(planeD,  Math.PI));
        twists.put(CubeMove.D3, new Twist(planeD, -Math.PI / 2));
        twists.put(CubeMove.U,  new Twist(planeU,  Math.PI / 2));
        twists.put(CubeMove.U2, new Twist(planeU,  Math.PI));
        twists.put(CubeMove.U3, new Twist(planeU, -Math.PI / 2));
        twists.put(CubeMove.F,  new Twist(planeF,  Math.PI / 2));
        twists.put(CubeMove.F2, new Twist(planeF,  Math.PI));
        twists.put(CubeMove.F3, new Twist(planeF, -Math.PI / 2));
        twists.put(CubeMove.B,  new Twist(planeB,  Math.PI / 2));
        twists.put(CubeMove.B2, new Twist(planeB,  Math.PI));
        twists.put(CubeMove.B3, new Twist(planeB, -Math.PI / 2));

        for (Move move : s.moves) {
            Twist t = twists.get(move);
            mesh = mesh.transformHalfspace(
                Matrix33.rotation(t.plane.n, t.angle),
                t.plane);
        }

        return mesh
            .transform(Matrix33.rotationY(-Math.PI / 6))
            .transform(Matrix33.rotationX(Math.PI / 7));
    }
}
