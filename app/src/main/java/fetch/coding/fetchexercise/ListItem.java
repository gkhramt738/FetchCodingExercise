package fetch.coding.fetchexercise;

import java.util.Objects;

public class ListItem implements Comparable<ListItem> {
    private final int id;
    private final int listId;
    private final String name;

    // JsonData constructor allowing for data save
    public ListItem(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    @Override
    // Generate a hashcode for String name for easy access
    public int hashCode() {
        return Objects.hash(getName());
    }

    // Getter method to retrieve id
    public int getId() {
        return id;
    }

    // Getter method to retrieve listId
    public int getListId() {
        return listId;
    }

    // Getter method to return String name
    public String getName() {
        return name;
    }

    @Override
    // Compare current listId item with target listId item
    public int compareTo(ListItem o) {
        if (o.listId > this.listId) {
            return -1;
        } else if (o.listId < this.listId) {
            return 1;
        }

        // Sort item names to list them in numerically ascending order
        // Source: https://stackoverflow.com/questions/54026739/how-to-sort-a-string-list-consists-of-digits-and-alphabets-in-java
        // Author: stackoverflow.com user "forpas" from answer on January 3, 2019

        String s1 = this.getName();
        String s2 = o.getName();

        if (s1.equalsIgnoreCase(s2)) {
            return 0;
        }

        String[] tokens1 = s1.split(" ");
        String[] tokens2 = s2.split(" ");

        if (!tokens1[0].equalsIgnoreCase(tokens2[0])) {
            return s1.compareToIgnoreCase(s2);
        }

        int number1 = Integer.parseInt(tokens1[1].replaceAll("\\D", ""));
        int number2 = Integer.parseInt(tokens2[1].replaceAll("\\D", ""));

        if (number1 != number2) {
            return number1 - number2;
        }
        // Source end

        // Make sort deterministic if multiple items have the same name but different IDs
        if (o.id > this.id) {
            return -1;
        } else if (o.id < this.id) {
            return 1;
        }
        return 0;
    }
}

