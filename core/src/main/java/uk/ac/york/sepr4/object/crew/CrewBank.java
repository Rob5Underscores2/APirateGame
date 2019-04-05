package uk.ac.york.sepr4.object.crew;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrewBank {

    private List<CrewMember> crew = new ArrayList<>();

    public CrewBank() {
        crew.add(new DoubleShotCrew());
    }

    public List<String> getCrewKeys() {
        List<String> keys = new ArrayList<>();
        crew.forEach(crewMember -> keys.add(crewMember.getKey()));
        return keys;
    }

    public Optional<CrewMember> getCrewFromID(Integer id) {
        for(CrewMember crewMember : crew) {
            if(crewMember.getId().equals(id)) {
                return Optional.of(crewMember);
            }
        }
        return Optional.empty();
    }

    public Optional<CrewMember> getCrewFromKey(String key) {
        for(CrewMember crewMember : crew) {
            if(crewMember.getKey().equalsIgnoreCase(key)) {
                return Optional.of(crewMember);
            }
        }
        return Optional.empty();
    }

}
