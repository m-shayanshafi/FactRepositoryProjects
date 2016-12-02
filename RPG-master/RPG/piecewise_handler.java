public abstract class piecewise_handler //unused class, intended to maintain a fixed y coordinate to allow the character to move on the map's ground
{
    public static int getY(int x, int map)
    {
        int y;
        switch (map)
        {
            case 0:
            {
                y = x < 500 ? 1200 : (x < 1000 ? 300 : 400);
                break;
            }
            default:
            y=0;
            break;
        }
        return y;
    }
}
