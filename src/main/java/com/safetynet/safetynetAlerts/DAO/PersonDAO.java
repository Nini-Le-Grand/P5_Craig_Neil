package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.data.JSONDataLoader;
import com.safetynet.safetynetAlerts.models.Firestation;
import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object (DAO) for managing {@link Person} java object.
 */
@Service
public class PersonDAO {
    @Autowired
    private JSONDataLoader jsonDataLoader;

    /**
     * Retrieves all {@link Person} from the data set.
     *
     * @return a list of all persons.
     */
    public List<Person> getPersons() {
        return jsonDataLoader.getJsonData().getPersons();
    }

    /**
     * Adds a new {@link Person} to the data set.
     *
     * @param person the person to be added.
     */
    public void addPerson(Person person) {
        getPersons().add(person);
    }

    /**
     * Updates the details of an existing {@link Person}.
     *
     * @param person the updated person details.
     * @param personToEdit the person to be edited.
     */
    public void setPerson(PersonUpdateDTO person, Person personToEdit) {
        if (!person.getAddress().isBlank()) personToEdit.setAddress(person.getAddress());
        if (!person.getCity().isBlank()) personToEdit.setCity(person.getCity());
        if (!person.getZip().isBlank()) personToEdit.setZip(person.getZip());
        if (!person.getPhone().isBlank()) personToEdit.setPhone(person.getPhone());
        if (!person.getEmail().isBlank()) personToEdit.setEmail(person.getEmail());
    }

    /**
     * Removes a {@link Person} from the data set.
     *
     * @param personToDelete the person to be removed.
     */
    public void removePerson(Person personToDelete) {
        getPersons().remove(personToDelete);
    }

    /**
     * Finds a {@link Person} by their first and last names.
     *
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     * @return an optional containing the person if found, otherwise empty.
     */
    public Optional<Person> findPerson(String firstName, String lastName) {
        return getPersons().stream().filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)).findAny();
    }

    /**
     * Collects {@link Person} living at any of the given addresses.
     *
     * @param addresses the list of addresses to filter persons by.
     * @return a list of persons living at the specified addresses.
     */
    public List<Person> collectOnAddresses(List<String> addresses) {
        return getPersons().stream().filter(person -> {
            for (String address : addresses) {
                if (person.getAddress().equals(address)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * Collects {@link Person} living at a specific address.
     *
     * @param address the address to filter persons by.
     * @return a list of persons living at the specified address.
     */
    public List<Person> collectOnAddress(String address) {
        return getPersons().stream().filter(person -> person.getAddress().equals(address)).toList();
    }

    /**
     * Collects {@link Person} living in a specific city.
     *
     * @param city the city to filter persons by.
     * @return a list of persons living in the specified city.
     */
    public List<Person> collectOnCity(String city) {
        return getPersons().stream().filter(person -> person.getCity().equals(city)).toList();
    }

    /**
     * Collects {@link Person} with a specific first and last name.
     *
     * @param firstName the first name to filter persons by.
     * @param lastName the last name to filter persons by.
     * @return a list of persons with the specified first and last name.
     */
    public List<Person> collectPerson(String firstName, String lastName) {
        return getPersons().stream().filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)).toList();
    }
}
