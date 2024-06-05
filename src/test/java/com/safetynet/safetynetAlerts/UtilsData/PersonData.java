package com.safetynet.safetynetAlerts.UtilsData;

import com.safetynet.safetynetAlerts.models.Person;
import com.safetynet.safetynetAlerts.models.PersonIdDTO;
import com.safetynet.safetynetAlerts.models.PersonUpdateDTO;

import java.util.ArrayList;
import java.util.List;

public class PersonData {
    public Person getPerson() {
        Person person = new Person();

        person.setFirstName("Neil");
        person.setLastName("Craig");
        person.setAddress("10 Downing Street");
        person.setCity("Paris");
        person.setZip("75017");
        person.setPhone("1111111111");
        person.setEmail("neil@craig.com");

        return person;
    }

    public PersonUpdateDTO getPersonUpdateDTO() {
        PersonUpdateDTO person = new PersonUpdateDTO();

        person.setFirstName("Neil");
        person.setLastName("Craig");
        person.setAddress("10 Downing Street");
        person.setCity("Paris");
        person.setZip("75017");
        person.setPhone("2222222222");
        person.setEmail("neil@craig.com");

        return person;
    }

    public PersonIdDTO getPersonIdDTO() {
        PersonIdDTO personIdDTO = new PersonIdDTO();
        personIdDTO.setFirstName("Neil");
        personIdDTO.setLastName("Craig");
        return personIdDTO;
    }

    public List<Person> getListOfPersons() {
        Person person1 = new Person();
        person1.setFirstName("Neil");
        person1.setLastName("Craig");
        person1.setAddress("10 Downing Street");
        person1.setCity("London");
        person1.setZip("54013");
        person1.setPhone("1111111111");
        person1.setEmail("neil@craig.com");

        Person person2 = new Person();
        person2.setFirstName("Laura");
        person2.setLastName("Delvine");
        person2.setAddress("12 Downing Street");
        person2.setCity("London");
        person2.setZip("54013");
        person2.setPhone("2222222222");
        person2.setEmail("laura@delvine.com");

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        return persons;
    }
}
