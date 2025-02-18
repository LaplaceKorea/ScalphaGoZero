package org.deeplearning4j.scalphagozero.experience

import org.nd4j.linalg.api.ndarray.INDArray
import scala.collection.mutable.ListBuffer

/**
  * Experience collector for AlphaGo Zero games. Collects encoded game states,
  * visit counts and rewards.
  *
  * @author Max Pumperla
  */
class ZeroExperienceCollector extends ExperienceCollector {

  val states: ListBuffer[INDArray] = ListBuffer.empty
  val visitCounts: ListBuffer[INDArray] = ListBuffer.empty
  val rewards: ListBuffer[INDArray] = ListBuffer.empty

  private val currentEpisodeStates: ListBuffer[INDArray] = ListBuffer.empty
  private val currentEpisodeVisitCounts: ListBuffer[INDArray] = ListBuffer.empty

  def clearAllBuffers(): Unit = {
    states.clear()
    visitCounts.clear()
    rewards.clear()
    clearEpisodeBuffers()
  }

  private def clearEpisodeBuffers(): Unit = {
    currentEpisodeStates.clear()
    currentEpisodeVisitCounts.clear()
  }

  override def beginEpisode(): Unit = clearEpisodeBuffers()

  override def recordDecision(state: INDArray, visitCounts: INDArray): Unit = {
    currentEpisodeStates += state
    currentEpisodeVisitCounts += visitCounts
    ()
  }

  override def completeEpisode(reward: INDArray): Unit = {
    states ++= currentEpisodeStates
    visitCounts ++= currentEpisodeVisitCounts
    for (_ <- 1 to currentEpisodeStates.size)
      rewards += reward

    clearEpisodeBuffers()
  }

}
