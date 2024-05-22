package com.safetynet.safetynetAlerts.DAO;

import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDAO {
    @Autowired
    private JSONDataDAO jsonDataDAO;

    public List<Person> getPersons() {
        return jsonDataDAO.getJsonData().getPersons();
    }

    public void addPerson(Person person) {
        getPersons().add(person);
    }

    public void setPerson(PersonUpdateDTO person, Person personToEdit) {
        if (!Objects.equals(person.getAddress(), "")) personToEdit.setAddress(person.getAddress());
        if (!Objects.equals(person.getCity(), "")) personToEdit.setCity(person.getCity());
        if (!Objects.equals(person.getZip(), "")) personToEdit.setZip(person.getZip());
        if (!Objects.equals(person.getPhone(), "")) personToEdit.setPhone(person.getPhone());
        if (!Objects.equals(person.getEmail(), "")) personToEdit.setEmail(person.getEmail());
    }

    public void removePerson(Person personToDelete) {
        getPersons().remove(personToDelete);
    }

    public Optional<Person> findPerson(String firstName, String lastName) {
        return getPersons()
                .stream()
                .filter(person ->
                        Objects.equals(person.getFirstName(), firstName)
                                && Objects.equals(person.getLastName(), lastName)
                )
                .findAny();
    }

    public List<Person> collectOnAddresses(List<String> addresses) {
        return getPersons()
                .stream()
                .filter(person -> {
                    for (String address : addresses) {
                        if (person.getAddress().equals(address)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Person> collectOnAddress(String address) {
        return getPersons().stream().filter(person -> person.getAddress().equals(address)).toList();
    }

    public List<Person> collectOnCity(String city) {
        return getPersons()
                .stream()
                .filter(person -> person.getCity().equals(city)).toList();
    }

    public List<Person> collectPerson(String firstName, String lastName) {
        return getPersons().stream().filter(person ->
                person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)).toList();
    }
}
