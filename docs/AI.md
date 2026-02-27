# AI-Assisted Development

This document records the use of AI tools in the development of Leo chatbot, as required for the **A-AiAssisted** increment.

---

## 🤖 AI Tools Used

| Tool | Purpose                                            |
|------|----------------------------------------------------|
| **Qwen Code (Alibaba)** | Code review, documentation, comment, test generation |
| **ChatGPT** | Debugging assistance                               |

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

**Tool:** Qwen Code and ChatGPT

**Tasks:**
- Reviewed entire codebase for coding standard violations
- Suggested SLAP (Single Level of Abstraction Principle) improvements

#### **2. Documentation Improvements**

**Tool:** Qwen Code

**Tasks:**
- Generated JavaDoc comments for methods lacking documentation
- Improved existing JavaDoc for clarity and completeness
- Created README.md and this AI.md sections for usage instructions

#### **3. Test Case Generation**

**Tool:** Qwen Code

**Tasks:**
- Generated JUnit test cases for existing methods
- Created edge case tests (empty input, invalid index, etc.)
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