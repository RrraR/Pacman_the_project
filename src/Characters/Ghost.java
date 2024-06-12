package Characters;

public interface Ghost {

    private void moveGhost(int targetX, int targetY){};
    private void loadImages(){}

    public int getGhostCordX();
    public int getGhostCordY();
    public GhostState getGhostState();

    public void resetPosition();
    public void ghostHasBeenEaten();

}
