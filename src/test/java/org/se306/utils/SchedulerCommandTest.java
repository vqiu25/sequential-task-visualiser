package org.se306.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

public class SchedulerCommandTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerCommandTest.class);

  @Test
  public void testMinimalValidCommand() {
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(command);

    int exitCode = cmdLine.execute("input.dot", "5");

    assertEquals(0, exitCode);
    assertEquals("input.dot", command.getInputFileName());
    assertEquals(5, command.getProcessors());
    assertEquals(1, command.getCores());
    assertEquals("input-output.dot", command.getOutputFileName());
    assertFalse(command.checkVisualise());
  }

  @Test
  public void testFullValidCommand() {
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(command);

    int exitCode = cmdLine.execute("input.dot", "5", "-p", "4", "-v", "-o", "output");

    assertEquals(0, exitCode);
    assertEquals("input.dot", command.getInputFileName());
    assertEquals(5, command.getProcessors());
    assertEquals(4, command.getCores());
    assertEquals("output.dot", command.getOutputFileName());
    assertNotEquals("output", command.getOutputFileName());
    assertTrue(command.checkVisualise());
  }

  @Test
  public void testValidCommandEqualsSyntax() {
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(command);

    int exitCode = cmdLine.execute("input.dot", "5", "-p=4", "-o=output");

    assertEquals(0, exitCode);
    assertEquals("input.dot", command.getInputFileName());
    assertEquals(5, command.getProcessors());
    assertEquals(4, command.getCores());
    assertEquals("output.dot", command.getOutputFileName());
    assertNotEquals("output", command.getOutputFileName());
    assertFalse(command.checkVisualise());
  }

  @Test
  public void testInvalidProcessorCount() {
    // Arrange
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(command);

    // Act
    int exitCode = cmdLine.execute("input.dot", "NaN");
    LOGGER.info("Exit code: {}", exitCode);

    // Assert
    assertNotEquals(0, exitCode);
  }
}
