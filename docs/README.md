# Leo - Task Manager Chatbot

![Ui](Ui.png)

## Introduction

Leo is a command-line task manager chatbot that helps you organize daily tasks with support for Todos, Deadlines, and Events.

## Quick Start

1. Download `leo.jar`
2. Run: `java -jar leo.jar`
3. Start typing commands!

## Commands

| Command | Example |
|---------|---------|
| `todo <description>` | `todo buy groceries` |
| `deadline <desc> /by <datetime>` | `deadline submit report /by 2025-12-31 2359` |
| `event <desc> /from <dt> /to <dt>` | `event meeting /from 2025-06-15 1400 /to 2025-06-15 1600` |
| `list` | `list` |
| `mark <number>` | `mark 1` |
| `unmark <number>` | `unmark 1` |
| `delete <number>` | `delete 1` |
| `find <keyword>` | `find meeting` |
| `undo` | `undo` |
| `bye` | `bye` |

## Date/Time Formats

- `2025-12-31` (date only)
- `2025-12-31 2359` (date + time)
- `31/12/2025 2359` (slash format)

## Features

- ✅ **Three task types**: Todo, Deadline, Event
- ✅ **Mark tasks done/not done**
- ✅ **Find tasks by keyword**
- ✅ **Undo last action**
- ✅ **Auto-save** to `data/leo.txt`

## System Requirements

- Java 17

---

*Leo - Your personal task management assistant*
