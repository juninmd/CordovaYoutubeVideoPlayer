```markdown
# AGENTS.md - Guidelines for AI Coding Agents

These guidelines are designed to ensure the creation of robust, maintainable, and efficient AI coding agents. Adherence to these principles is crucial for the long-term success and quality of this repository.

## 1. DRY (Don't Repeat Yourself)

*   **Single Responsibility Principle:** Each agent should have a clearly defined and single responsibility. Avoid creating agents with duplicated logic or functionalities.
*   **Module Reuse:** Reuse existing code snippets and components across multiple agents whenever possible.
*   **Abstraction:** Introduce abstraction layers to encapsulate complex logic and make it easier to modify.

## 2. KISS (Keep It Simple, Stupid)

*   **Minimal Code:** Strive for the shortest possible code to achieve a specific functionality. Avoid unnecessary complexity.
*   **Readability:** Prioritize code readability. Use clear variable names, comments, and formatting.
*   **Simplicity:** Design components with simple, understandable architectures.

## 3. SOLID Principles

*   **Single Responsibility:** As mentioned above, each component has a single, well-defined purpose.
*   **Open/Closed Principle:**  The agent’s design should be open for extension (adding new functionalities) without modifying existing code. Closed for modification (improving or fixing existing code).
*   **Liskov Substitution Principle:**  Subclasses should be substitutable for their base classes without affecting the correctness of the program.
*   **Interface Segregation Principle:**  Clients should not be forced to depend on methods they do not use.
*   **Dependency Inversion Principle:**  High-level modules should be dependent on low-level modules, and modules should be dependent on abstractions.

## 4. YAGNI (You Aren't Gonna Need It)

*   **Avoid Feature Creep:** Don’t add functionality that is not currently required.  Refactor to existing functionality as needed.
*   **Future-Proofing:** Design agents to be adaptable to changing requirements, while avoiding unnecessary complexity.

## 5. Development Practices

*   **Code Reviews:**  All agent code must undergo mandatory code reviews before merging.
*   **Unit Testing:** Comprehensive unit tests are required for all agent components.  Tests should cover all critical functionalities.
*   **Integration Testing:**  Detailed integration tests must be performed to ensure agent interactions are working correctly.
*   **Documentation:**  Clear and concise documentation should be provided for each agent component.  Include API specifications and usage examples.
*   **Error Handling:** Implement robust error handling to prevent crashes and provide informative error messages.
*   **Logging:** Use appropriate logging to track agent behavior and potential issues.
*   **Dependency Management:**  Utilize a dependency management system (e.g., Maven, pip) to manage external libraries.

## 6. Code Length Constraint (180 lines)

*   **Maximum Code Length:**  Each file should not exceed 180 lines of code.
*   **Code Splitting:** Implement code splitting to optimize performance.

## 7. Test Coverage (80% Minimum)

*   **Comprehensive Tests:** Aim for at least 80% code coverage through unit, integration, and potentially end-to-end tests.
*   **Test Case Design:**  Test cases should be well-designed, covering various scenarios, boundary conditions, and error conditions.
*   **Test Data:** Use realistic test data that accurately reflects the expected input and output of the agent.

## 8. File Structure (Example)

*   **AgentName.py:** Contains core agent logic.
*   **DataManagement.py:** Handles data loading and storage (if required).
*   **Interface.py:** Defines the interface for interacting with the agent.
*   **UnitTests.py:** Contains automated unit tests.
*   **Documentation.md:**  Provides documentation for each agent component.

## 9.  Additional Considerations

*   **Configuration Management:**  Implement a mechanism for managing agent configurations.
*   **Version Control:**  Utilize a version control system (e.g., Git) for code management.
*   **Code Style:**  Follow a consistent code style guide (e.g., PEP 8).
*   **Security:**  Prioritize security considerations throughout the agent development process.

These guidelines are intended as a starting point and may require adjustments based on specific project needs. Continuous monitoring and adaptation will ensure the long-term maintainability and quality of the AGENTS.md repository.
```