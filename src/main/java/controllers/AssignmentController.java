package controllers;

import java.util.ArrayList;
import models.member.IMember;
import models.member.Member;
import models.project.Project;
import models.task.Task;
import util.ParserHelper;

//@@author sinteary
public class AssignmentController {
    private ArrayList<String> errorMessages;
    private ArrayList<String> successMessages;
    private ParserHelper parserHelper;
    private Project project;

    /**
     * A Controller class which parses the input from the user to get a list of valid
     * task and member indexes, and manages the assignment to and removal of tasks from members.
     * @param project The which the user is managing.
     */
    public AssignmentController(Project project) {
        this.errorMessages = new ArrayList<>();
        this.successMessages = new ArrayList<>();
        this.parserHelper = new ParserHelper();
        this.project = project;
    }

    /**
     * Does the actual assignment of task by calling on the project and establishing the
     * necessary links between tasks and members. Collates messages to inform user of
     * successful assignments/unassignments.
     * @param input The input from the user.
     */
    public void assignAndUnassign(String input) {
        if (input.length() < 12) {
            errorMessages.add("Insufficient parameters!"
                + "Indicate the tasks and members whom you wish to assign or remove!");
            errorMessages.add("Format is \"assign task -i TASK_INDEX -to [MEMBER_INDEX] -rm [MEMBER_INDEX]\"");
            errorMessages.add("You must either assign a task to someone, or remove, or both!");
            return;
        }
        assert input.length() >= 12 : "Valid input";
        input = input.substring(12); //remove the "assign task " portion
        ArrayList<ArrayList<Integer>> assignmentParams = parserHelper.parseAssignmentParams(input, project);
        errorMessages.addAll(parserHelper.getErrorMessages());
        ArrayList<Integer> validTaskIndexes = assignmentParams.get(0);
        ArrayList<Integer> validAssignees = assignmentParams.get(1);
        ArrayList<Integer> validUnassignees = assignmentParams.get(2);

        if (validTaskIndexes.size() == 0) {
            errorMessages.add("No valid task numbers detected. Cannot assign any tasks.");
            errorMessages.add("Please input valid task numbers in this format: -i TASK_INDEX");
            return;
        }
        assert validTaskIndexes.size() > 0 : "Valid tasks exist";

        if (validAssignees.size() == 0 && validUnassignees.size() == 0) {
            errorMessages.add("Insufficient parameters! Indicate the members whom you wish to assign or remove!");
            errorMessages.add("Format is \"assign task -i TASK_INDEX -to [MEMBER_INDEX] -rm [MEMBER_INDEX]\"");
            errorMessages.add("You must either assign a task to someone, or remove, or both!");
            return;
        }
        assert validAssignees.size() > 0 || validUnassignees.size() > 0 : "Valid members exist";

        for (Integer taskIndex : validTaskIndexes) {
            Task task = project.getTask(taskIndex);
            successMessages.add("For task " + taskIndex + " (" + task.getTaskName() + "):");
            //assigning tasks
            if (!validAssignees.isEmpty()) {
                assign(validAssignees, task);
            }
            //unassigning tasks
            if (!validUnassignees.isEmpty()) {
                unassign(validUnassignees, task);
            }
        }
    }


    /**
     * Assign valid members to a particular task and updates the associated success or error messages
     * relating to the assignment.
     * @param validAssignees ArrayList containing valid member index numbers, for assignees.
     * @param task Task to be assigned to members.
     */
    private void assign(ArrayList<Integer> validAssignees, Task task) {
        ArrayList<String> assignMessages = new ArrayList<>();
        for (Integer assigneeIndex : validAssignees) {
            IMember member = project.getMember(assigneeIndex);
            if (project.containsAssignment(task, (Member)member)) {
                assignMessages.add("Task has already been assigned to member "
                    + assigneeIndex + " ("
                    + member.getName() + ").");
            } else {
                project.createAssignment(task, (Member)member);
                assignMessages.add("Assigned to member "
                    + assigneeIndex + " ("
                    + member.getName() + ").");
            }
        }
        successMessages.addAll(assignMessages);
    }

    /**
     * Removes valid members from a particular task, and updates the associated success or error messages
     * relating to the removal of assignment.
     * @param validUnassignees ArrayList containing valid member index numbers, for unassignees.
     * @param task Task to be unassigned.
     */
    private void unassign(ArrayList<Integer> validUnassignees, Task task) {
        ArrayList<String> unassignMessages = new ArrayList<>();
        for (Integer unassigneeIndex : validUnassignees) {
            IMember member = project.getMembers().getMember(unassigneeIndex);
            if (!project.containsAssignment(task, (Member)member)) {
                unassignMessages.add("Task cannot be unassigned from member "
                    + unassigneeIndex + " (" + member.getName() + ") as it was "
                    + "not assigned in the first place!");
            } else {
                project.removeAssignment((Member)member, task);
                unassignMessages.add("Unassigned task from member " + unassigneeIndex
                    + " (" + member.getName() + ").");
            }
            successMessages.addAll(unassignMessages);
        }
    }

    /**
     * Returns messages about invalid inputs to be shown to the user.
     * @return an ArrayList of error messages that arise from invalid inputs.
     */
    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Returns messages about successful assignments to be shown to the user.
     * @return an ArrayList of success messages indicating successful assignment.
     */
    public ArrayList<String> getSuccessMessages() {
        return successMessages;
    }
}
