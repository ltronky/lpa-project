package it.ltronchi.lpa
import scala.actors.Actor

object AutomaticEsecutor extends Actor {
	var esecuting = true
	
	def act() = {
		esecuting = false
		loop {
			
			receiveWithin(10) {
				case "p" => {if (esecuting) esecuting = false else esecuting = true}
				case "a" => esecuting = false
				case _ =>
			}
			
			if (esecuting)
			try {
				Main.next()
				Thread.sleep((Main.sleepTime*1000/Main.speedCoef).toLong)
			} catch {
				case ie: InterruptedException => 
			}
			
		}
	}
}