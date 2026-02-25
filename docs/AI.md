# AI-Assisted Development

This document records the use of AI tools in the development of Leo chatbot, as required for the **A-AiAssisted** increment.

---

## 🤖 AI Tools Used

| Tool | Purpose |
|------|---------|
| **Qwen Code (Alibaba)** | Code review, refactoring, documentation, test generation |
| **GitHub Copilot** | Auto-completion, method generation |
| **ChatGPT** | Debugging assistance, code explanations |

---

## 📝 How AI Tools Were Used

### **Phase 1: Before A-AiAssisted Increment**

All code up to and including the following increments was written **manually without AI assistance**:

- Level-0 through Level-10
- A-MoreOOP, A-Packages, A-Gradle, A-JUnit, A-Jar
- A-JavaDoc, A-CodingStandard, A-CheckStyle
- A-Assertions, A-CodeQuality, A-Streams
- C-Undo (extension)

---

### **Phase 2: A-AiAssisted Increment**

Starting from this increment, AI tools were used to:

#### **1. Code Review and Quality Improvements**

**Tool:** Qwen Code

**Tasks:**
- Reviewed entire codebase for coding standard violations
- Identified magic numbers and suggested extraction to constants
- Recommended guard clauses to reduce nesting
- Suggested SLAP (Single Level of Abstraction Principle) improvements

#### **2. Documentation Improvements**

**Tool:** Qwen Code, GitHub Copilot

**Tasks:**
- Generated JavaDoc comments for methods lacking documentation
- Improved existing JavaDoc for clarity and completeness
- Added `@author` and `@version` tags to class documentation
- Created README.md sections for usage instructions

#### **3. Test Case Generation**

**Tool:** GitHub Copilot, Qwen Code

**Tasks:**
- Generated JUnit test cases for existing methods
- Created edge case tests (empty input, invalid index, etc.)
- Added tests for new C-Undo functionality

#### **4. README.md Updates**

**Tool:** Qwen Code

**Tasks:**
- Added comprehensive usage instructions
- Created command reference table
- Added troubleshooting section
- Documented JavaFX setup for Mac users

#### **5. Code Quality Suggestions**

**Tool:** Qwen Code

**Recommendations Implemented:**
- Extracted magic numbers to named constants (`TODO_PREFIX_LENGTH`, `EVENT_PREFIX_LENGTH`)
- Used intermediate boolean variables for complex conditions
- Applied guard clauses to make happy path prominent
- Extracted helper methods to follow SLAP principle

---

## 📊 Impact of AI Assistance

| Aspect | Before AI | After AI |
|--------|-----------|----------|
| **Code Quality** | Manual review | Automated suggestions + manual review |
| **Documentation** | Basic JavaDoc | Comprehensive JavaDoc with examples |
| **Test Coverage** | Manual tests | AI-generated edge cases |
| **Development Speed** | Standard | Accelerated for repetitive tasks |
| **Learning** | Self-discovery | AI as pair programmer |

---

## 📚 References

- [SE-EDU Guide on AI-Assisted Coding](https://se-education.org/guides)
- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [Effective Prompt Engineering for Code](https://learnprompting.org)
