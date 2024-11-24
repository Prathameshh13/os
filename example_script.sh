

#!/bin/bash

ADDRESS_BOOK="address_book.txt"

# Function to display menu
show_menu() {
    echo "1. Add Contact"
    echo "2. List Contacts"
    echo "3. Search Contact"
    echo "4. Delete Contact"
    echo "5. Modify Contact"
    echo "6. Exit"
}

# Function to add a contact
add_contact() {
    echo "Enter Name:"
    read name
    echo "Enter Phone:"
    read phone
    echo "Enter Email:"
    read email
    echo "$name:$phone:$email" >> $ADDRESS_BOOK
    echo "Contact added successfully!"
}

# Function to list all contacts
list_contacts() {
    echo "Contacts:"
    while IFS=":" read -r name phone email; do
        echo "Name: $name, Phone: $phone, Email: $email"
    done < $ADDRESS_BOOK
}

# Function to search for a contact
search_contact() {
    echo "Enter name to search:"
    read search_name
    grep -i "$search_name" $ADDRESS_BOOK | while IFS=":" read -r name phone email; do
        echo "Name: $name, Phone: $phone, Email: $email"
    done
}

# Function to delete a contact
delete_contact() {
    echo "Enter name to delete:"
    read delete_name
    grep -iv "$delete_name" $ADDRESS_BOOK > temp.txt && mv temp.txt $ADDRESS_BOOK
    echo "Contact deleted successfully!"
}

# Function to modify a contact
modify_contact() {
    echo "Enter name of the contact to modify:"
    read modify_name
    if grep -iq "$modify_name" $ADDRESS_BOOK; then
        # Read current details
        current_contact=$(grep -i "$modify_name" $ADDRESS_BOOK)
        IFS=":" read -r current_name current_phone current_email <<< "$current_contact"

        echo "What would you like to modify?"
        echo "1. Name"
        echo "2. Phone"
        echo "3. Email"
        echo "4. All"
        read modify_choice

        case $modify_choice in
            1)
                echo "Enter new Name:"
                read new_name
                new_contact="$new_name:$current_phone:$current_email"
                ;;
            2)
                echo "Enter new Phone:"
                read new_phone
                new_contact="$current_name:$new_phone:$current_email"
                ;;
            3)
                echo "Enter new Email:"
                read new_email
                new_contact="$current_name:$current_phone:$new_email"
                ;;
            4)
                echo "Enter new Name:"
                read new_name
                echo "Enter new Phone:"
                read new_phone
                echo "Enter new Email:"
                read new_email
                new_contact="$new_name:$new_phone:$new_email"
                ;;
            *)
                echo "Invalid choice."
                return
                ;;
        esac

        # Remove old contact and add updated contact
        grep -iv "$modify_name" $ADDRESS_BOOK > temp.txt && mv temp.txt $ADDRESS_BOOK
        echo "$new_contact" >> $ADDRESS_BOOK
        echo "Contact modified successfully!"
    else
        echo "Contact not found!"
    fi
}

# Main script loop
while true; do
    show_menu
    read choice
    case $choice in
        1) add_contact ;;
        2) list_contacts ;;
        3) search_contact ;;
        4) delete_contact ;;
        5) modify_contact ;;
        6) exit 0 ;;
        *) echo "Invalid option, please try again." ;;
    esac
done

#cd /c/Users/HP/Desktop
#bash example_script.sh
#chmod +x example_script.sh
#./example_script.sh
