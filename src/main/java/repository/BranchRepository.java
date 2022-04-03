package repository;

import global.Globals;
import model.Branch;

import java.util.Optional;

public class BranchRepository {

    public Branch saveBranch(Branch branch) {
        Globals.branches.add(branch);
        return branch;
    }

    public Optional<Branch> findBranchByName(String name) {
        return Globals.branches
                .stream()
                .filter(branch -> branch.getName().equals(name))
                .findFirst();
    }

}
