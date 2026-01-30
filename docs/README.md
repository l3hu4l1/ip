# Pixel User Guide

![Pixel Logo](pixel.png)

Pixel is a CLI task assistant that helps you keep track of your todos,
deadlines, and events. All tasks are automatically saved to disk and loaded when
you restart the application.

## Features

- Add three types of tasks: todos, deadlines, and events
- Mark tasks as done or not done
- Delete tasks from your list
- Tasks are saved automatically
- Display all your tasks with status indicators

## Quick start

1. Ensure you have Java 11 or above installed.
2. Build and run:
    ```bash
    $ javac -d bin src/main/java/*.java
    $ java -classpath bin Pixel
    ```
3. Type commands followed by Enter.
4. Your tasks are automatically saved to `./data/pixel.txt`

## Commands Summary

| Command       | Format                                                            |
| ------------- | ----------------------------------------------------------------- |
| Add todo      | `todo DESCRIPTION`                                                |
| Add deadline  | `deadline DESCRIPTION /by yyyy-MM-dd [HHmm]`                      |
| Add event     | `event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]` |
| List tasks    | `list`                                                            |
| Mark done     | `mark INDEX`                                                      |
| Mark not done | `unmark INDEX`                                                    |
| Delete task   | `delete INDEX`                                                    |
| Exit          | `bye`                                                             |

## Feature Details

### Add various task types

Pixel supports three types of tasks:

- **Todo [T]**: Simple tasks
- **Deadline [D]**: Tasks with a deadline
- **Event [E]**: Tasks with a start and end date/time

```
$ todo borrow book
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________

$ deadline return book /by 2026-06-15 1800
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Jun 15 2026 18:00)
 Now you have 2 tasks in the list.
____________________________________________________________

$ event project meeting /from 2026-08-20 1400 /to 2026-08-20 1600
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 20 2026 14:00 to: Aug 20 2026 16:00)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Display all tasks

View all your tasks with their status:

```
$ list
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Jun 15 2026 18:00)
 3.[E][ ] project meeting (from: Aug 20 2026 14:00 to: Aug 20 2026 16:00)
____________________________________________________________
```

### Mark and unmark tasks

Track completion status with `[X]` for done and `[ ]` for not done:

```
$ mark 2
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Jun 15 2026 18:00)
____________________________________________________________

$ unmark 2
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Jun 15 2026 18:00)
____________________________________________________________
```

### Delete tasks

Remove tasks you no longer need:

```
$ delete 3
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Aug 20 2026 14:00 to: Aug 20 2026 16:00)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Data Persistence

All your tasks are automatically saved to `./data/pixel.txt` whenever you:

- Add a new task
- Mark/unmark a task
- Delete a task

When you restart Pixel, your tasks are automatically loaded. The data file uses
this format:

```
T | 1 | read book
D | 0 | return book | 2026-06-06 1800
E | 0 | project meeting | 2026-08-06 1400 | 2026-08-06 1600
```

- First field: Task type (T/D/E)
- Second field: Status (1=done, 0=not done)
- Remaining fields: Task details (dates stored as yyyy-MM-dd HHmm)

## Error Handling

Pixel handles various error cases gracefully:

- Empty descriptions: `OOPS!!! The description cannot be empty.`
- Missing parameters: `OOPS!!! A deadline must include /by.`
- Invalid task numbers: `OOPS!!! The task index is invalid.`
- Unknown commands: `OOPS!!! I'm sorry, but I don't know what that means :-(`
