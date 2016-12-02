public class Inventory
{
    private static TextIO t = new TextIO(); //static because only one
    private static Item[] backpack = new Item[25];
    public static void modify(Entity a){
        t.clear();
        int index = 0;
        int response = 0;
        if (!isEmpty()){
            t.getString("Your inventory is empty. Press enter to continue playing."); //check if its empty and cant change anything
        }
        while (index < backpack.length){
            if (backpack[index] != null){
                System.out.println((index + 1) + " " + backpack[index].getName()); //print all items
            }
            index++;
        }
        while (response != 42 && response < backpack.length && response >= 0 && backpack[response] != null) {
            t.blank(1);
            modulateItem(t.getInt("Simply type the number of the item you would like to examine. To exit, type 42."),a); //lets you modulate an item given proper input
            modify(a);
        }
    }

    public static void addItem(Item i){
        for (int a = 0; a < backpack.length; a++){
            if (backpack[a] == null){
                backpack[a] = i; //find first empty space and make it the item
                break;
            }
        }
        if (isFull()){
            t.say("Your backpack is full. Remove an item or something. "); //if full
        }
    }

    public static Item getPotion()
    {
        int index = 0;
        int response = 0;
        int[] shift = new int[25];
        int shiftIndex = 0;
        t.say("Pick a potion:\n");

        while (index < backpack.length){
            if (backpack[index] instanceof Potion){
                System.out.println((shiftIndex + 1) + " " + backpack[index].getName()); //prints potions
                shift[shiftIndex] = index;
                shiftIndex++;
            }
            index++;
        }
        response = t.getInt("Pick a potion.");
        return backpack[shift[response]];

    }
    
    public static Item[] getListOfPotions() {
        return backpack;
    }
    
    public static void setListOfPotions(Item[] i)   {
        backpack = i;
    }

    public static void modulateItem(int index, Entity character){
        Item i = backpack[index - 1];
        i.explicate(t); //accesses overloaded method from items to print out name and desc of item
        t.blank(1);
        String response = t.getString("Type rm if you want to remove this item. Type equip to equip the item. Press enter to view your inventory.");
        if (response.equals("rm")){
            for (int a = index-1; a<24;a++){
                backpack[a] = backpack[a+1]; //when its removed, move everything else down
            }
        }
        else {
            if(response.equals("equip")){
                i = character.replace(i);  //replace the item in loadout with this one
            }
        }
    }

    public static boolean isFull(){
        return (backpack[24] != null);  //if its full
    }

    public static boolean isEmpty(){
        return (backpack[0] != null);  //if its empty
    }

    public static boolean potionExists(){
        boolean potionExists = false;
        for (int i = 0; i < backpack.length; i++){
            if (backpack[i] instanceof Potion){
                potionExists = true;  //check if potion exists, duh
            }
        }
        return potionExists;
    }
}
