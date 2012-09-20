package it.ltronchi.lpa

import scala.swing.Component
import java.awt.Graphics2D
import java.awt.Color
import scala.swing.event.MousePressed
import java.awt.Dimension
import scala.swing._
import javax.swing.border.LineBorder
import scala.swing.event.MouseClicked
import java.awt.Font
import scala.collection.Set
import scala.actors.Actor

object MyCell {
	var worldX = 0;
	var worldY = 0;
}


class MyCell(val x:Int, val y:Int) extends FlowPanel with Runnable {


	contents += new Label("")
	val lock:AnyRef = new Object();
	val lockExe:AnyRef = new Object();

	border = LineBorder.createGrayLineBorder

			var _living = false
			background = Color.WHITE


			var neighbors =  Set.empty[Array[Int]];
	for (i <- -1 to 1) {
		for (j <- -1 to 1) {
			if (!(i==0 && j==0))
				neighbors += Array((x + MyCell.worldX + i) % MyCell.worldX, (y + MyCell.worldY + j) % MyCell.worldY)
		}
	}

	listenTo(mouse.clicks) 
	reactions += {
	case e: MouseClicked =>{
		if (living) {
			living = false
					background = Color.WHITE
		}
		else {
			living = true 
					background = Color.BLACK
		}
		repaint()
	}
	}

	def living = lock.synchronized {_living}

	def living_= (alive:Boolean) = _living = lock.synchronized{
		if (alive == true) {
			background = Color.BLACK
		} else {
			background = Color.WHITE
		}	
		alive
	}

	def next() {
		var aliveNeighborsCounter = 0;
		neighbors foreach(item => if (Main.grid.contents(item(1)*MyCell.worldX + item(0)) match {
		case value:MyCell =>  {
			value.living
		}
		})aliveNeighborsCounter += 1 )


		if (living && aliveNeighborsCounter < 2) living = false
		if (living && aliveNeighborsCounter > 3) living = false
		if (!living && aliveNeighborsCounter == 3) living = true 
	}


	var _executing = true;
	def executing = lockExe.synchronized {_executing}
	def executing_= (value:String) = _executing = lockExe.synchronized {value match {
	  case "Alt" => false
	  case "Play/Pause" => if (_executing) false else true
	  }}
	
	def run() = {
//		println("started" + (y*MyCell.worldY + x))
		while(true) {
		  
		Thread.sleep((Main.sleepTime*1000/Main.speedCoef).toLong)

		  if (executing)
			try {
				next()
//				println(y*MyCell.worldY + x)
			} catch {
			case ie: InterruptedException => 
			}

		}
	}

}