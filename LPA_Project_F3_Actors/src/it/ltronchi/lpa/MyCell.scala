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
import scala.actors.TIMEOUT

object MyCell {
	var worldX = 0;
	var worldY = 0;
}


class MyCell(val x:Int, val y:Int) extends FlowPanel with Actor {
	
	
	contents += new Label("")
	
	border = LineBorder.createGrayLineBorder
	
	var _living = false
	background = Color.WHITE
	
	var neighbors = Set.empty[MyCell];
	var _livingNeibors = 0
	
	def livingNeibors = _livingNeibors
	def livingNeibors_= (liv:Int) = _livingNeibors = {updateContent(liv);liv} 

	def initi() {
		for (i <- -1 to 1) {
			for (j <- -1 to 1) {
				if (!(i==0 && j==0)){
					var cmp = Main.grid.contents(((x + MyCell.worldX + i) % MyCell.worldX) +( (y + MyCell.worldY + j) % MyCell.worldY)*MyCell.worldX) match { case cell:MyCell => cell}
					neighbors += cmp
				}
			}
		}
	}
	
	def updateContent(liv:Int) = {contents(0) match {case l:Label => l.text=liv.toString; if(living) l.foreground = Color.WHITE else l.foreground = Color.BLACK}}
	
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
	
	def living = _living
	
	def living_= (alive:Boolean) = _living = {
		if (alive == true) {
			background = Color.BLACK
		} else {
			background = Color.WHITE
		}
		updateContent(livingNeibors)
		alive
	}
	
	def reloadLiving() = livingNeibors = neighbors.filter(_.living == true).size
	
	def next() {
		if (living && livingNeibors < 2) {
			living = false
			neighbors.foreach(c => c ! "dead")
		}
		if (living && livingNeibors > 3) {
			living = false
			neighbors.foreach(c => c ! "dead")
		}
		if (!living && livingNeibors == 3) {
			living = true
			neighbors.foreach(c => c ! "alive")
		}
	}
	
	
	var esecuting = false	
	def act() = {
		loop {	
			reactWithin(10) {
				case "p" => {if (esecuting) esecuting = false else esecuting = true}
				case "a" => esecuting = false
				case "dead" => livingNeibors-=1
				case "alive" => livingNeibors+=1
				case _=>// println(c.toString)
			}
			
			try {
				if (esecuting) next()
				Thread.sleep((Main.sleepTime*1000/Main.speedCoef).toLong)
			} catch {
				case ie: InterruptedException => 
			}
			
		}
	}
	
}