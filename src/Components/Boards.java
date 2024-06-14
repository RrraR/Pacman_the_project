package Components;

import static Components.GameBoard.*;

public class Boards {

    public final static int W=1; // Wall
    public final static int F=2; // Food
    public final static int E=3; // Empty
    public final static int D=4; // Door
    public final static int U=5; // Ghost Upgrade
    public final static int P=6; // Power Pellet

    public static int[][] board23x24 = {
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
            {W,P,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,P,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,F,W},
            {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
            {W,W,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,W,W},
            {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
            {E,E,E,E,W,F,W,F,W,W,W,D,W,W,W,F,W,F,W,E,E,E,E},
            {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
            {F,F,F,F,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,F,F,F,F},
            {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
            {E,E,E,E,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,E,E,E,E},
            {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
            {W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
            {W,P,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,P,W},
            {W,W,W,F,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,F,W,W,W},
            {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
            {W,F,W,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    public static int[][] board27x18 = {
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,F,W,F,W,W,W,W,W,W,W,W,W,F,W,F,W,W,W,W,F,W},
            {W,P,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,P,W},
            {W,F,W,W,F,W,W,W,F,W,W,W,F,W,F,W,W,W,F,W,W,W,F,W,W,F,W},
            {W,F,W,W,F,F,F,W,F,W,F,F,F,W,F,F,F,W,F,W,F,F,F,W,W,F,W},
            {W,F,W,W,W,W,F,W,W,W,F,W,W,W,W,W,F,W,W,W,F,W,W,W,W,F,W},
            {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
            {W,W,W,W,F,W,W,W,W,F,W,W,W,D,W,W,W,F,W,W,W,W,F,W,W,W,W},
            {E,E,E,W,F,W,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,W,F,W,E,E,E},
            {E,E,E,W,F,W,W,W,W,F,W,E,E,E,E,E,W,F,W,W,W,W,F,W,E,E,E},
            {E,E,E,W,F,F,F,F,W,F,W,E,E,E,E,E,W,F,W,F,F,F,F,W,E,E,E},
            {W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W},
            {W,F,F,P,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,P,F,F,W},
            {W,F,W,W,F,W,W,W,F,W,W,W,F,W,F,W,W,W,F,W,W,W,F,W,W,F,W},
            {W,F,W,W,F,W,F,F,F,F,F,W,F,W,F,W,F,F,F,F,F,W,F,W,W,F,W},
            {W,F,F,F,F,F,F,W,W,W,F,F,F,F,F,F,F,W,W,W,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}

    };

    public static int[][] board21x21 = {
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F},
            {W,W,F,W,F,W,W,W,W,W,F,W,W,W,W,W,F,W,F,W,W},
            {W,P,F,F,F,F,F,W,F,F,F,F,F,W,F,F,F,F,F,P,W},
            {W,W,W,F,W,W,F,W,F,W,F,W,F,W,F,W,W,F,W,W,W},
            {W,F,F,F,W,W,F,F,F,W,F,W,F,F,F,W,W,F,F,F,W},
            {W,F,W,F,F,F,F,W,W,W,F,W,W,W,F,F,F,F,W,F,W},
            {W,F,F,F,W,W,F,F,F,F,F,F,F,F,F,W,W,F,F,F,W},
            {W,W,F,W,E,W,F,W,W,W,D,W,W,W,F,W,E,W,F,W,W},
            {W,W,F,W,W,W,F,W,E,E,E,E,E,W,F,W,W,W,F,W,W},
            {F,F,F,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,F,F,F},
            {W,W,F,W,W,W,F,W,E,E,E,E,E,W,F,W,W,W,F,W,W},
            {W,W,F,W,E,W,F,W,W,W,W,W,W,W,F,W,E,W,F,W,W},
            {W,F,F,F,W,W,F,F,F,F,F,F,F,F,F,W,W,F,F,F,W},
            {W,F,W,F,F,F,F,W,W,W,F,W,W,W,F,F,F,F,W,F,W},
            {W,F,F,F,W,W,F,F,F,W,F,W,F,F,F,W,W,F,F,F,W},
            {W,W,W,F,W,W,F,W,F,W,F,W,F,W,F,W,W,F,W,W,W},
            {W,P,F,F,F,F,F,W,F,F,F,F,F,W,F,F,F,F,F,P,W},
            {W,W,F,W,F,W,W,W,W,W,F,W,W,W,W,W,F,W,F,W,W},
            {F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    public static int[][] board31x11 = {
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,P,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,F,W,W,W,F,W,W,W,W,W,W,W,F,W,W,W,F,W,F,W,W,W,F,W},
            {W,F,W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,W,F,W},
            {W,F,W,F,W,W,W,W,W,F,W,F,W,W,W,D,W,W,W,F,W,F,W,W,W,W,W,F,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,F,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,F,W,F,W},
            {W,F,W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,W,F,W},
            {W,F,W,W,W,F,W,F,W,W,W,F,W,W,W,W,W,W,W,F,W,W,W,F,W,F,W,W,W,F,W},
            {W,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,P,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    public static int[][] board15x21 = {
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,P,W},
            {W,F,W,W,F,W,W,F,W,W,F,W,W,F,W},
            {W,F,W,W,F,W,W,F,W,W,F,W,W,F,W},
            {W,F,W,F,F,F,F,F,F,F,F,F,W,F,W},
            {W,F,F,F,W,W,F,W,F,W,W,F,F,F,W},
            {W,F,W,F,W,W,F,W,F,W,W,F,W,F,W},
            {W,F,W,F,F,F,F,W,F,F,F,F,W,F,W},
            {W,F,W,W,W,W,F,W,F,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,F,W,W,W,D,W,W,W,F,W,F,W},
            {W,F,W,F,W,E,E,E,E,E,W,F,W,F,W},
            {W,F,W,F,W,E,E,E,E,E,W,F,W,F,W},
            {W,F,W,F,W,E,E,E,E,E,W,F,W,F,W},
            {W,F,W,F,W,W,W,W,W,W,W,F,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,F,W,F,W,W,W,W,F,W},
            {W,F,W,F,F,F,F,W,F,F,F,F,W,F,W},
            {W,F,W,F,W,W,F,W,F,W,W,F,W,F,W},
            {W,F,F,F,W,W,F,W,F,W,W,F,F,F,W},
            {W,F,W,F,F,F,F,F,F,F,F,F,W,F,W},
            {W,F,W,W,F,W,W,F,W,W,F,W,W,F,W},
            {W,F,W,W,F,W,W,F,W,W,F,W,W,F,W},
            {W,P,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };
}
