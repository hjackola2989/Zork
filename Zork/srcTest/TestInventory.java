import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestInventory {

	Inventory thisInv;
	
	@Before
	public void initInventory()
	{
		thisInv = new Inventory();
		thisInv.add(new Item("Gate Key", 0, 0, 7));
		thisInv.add(new Item("Shield", 1, 5, -1));
		thisInv.add(new Item("Sword", 5, 1, -1));
		thisInv.add(new Item("Suit of Armor", -1, 7, -1));
	}
	
	@Test
	public void testFind() {
		assertEquals(3, thisInv.find("Armor"));
	}
	
	@Test
	public void testSize() {
		thisInv.display();
		assert(thisInv.getSize()==4);
	}

	@Test
	public void testRemove() {
		thisInv.remove(1);
		thisInv.display();
		assert(thisInv.getSize()==3);
		assert(thisInv.get(1).getName().equals("Sword"));
	}
	
	@Test
	public void testPickup()
	{
		int s = thisInv.getSize(); 
		Inventory newInv = new Inventory();
		newInv.pickUp(thisInv, 1);
		assertEquals(1, newInv.getSize());
		assertEquals(s-1, thisInv.getSize());

	}
	
	
	
}
