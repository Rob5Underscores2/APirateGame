package uk.ac.york.sepr4.object.building;

import lombok.Data;

@Data
public class Department extends Building {

    private Double fix_rate;
    private College allied;

    public Department() {
        // Empty constructor for JSON DAO
    }

}
