package it.ltronchi.lpa


import scala.collection.mutable.Set
import scala.swing.SimpleSwingApplication
import scala.swing.MainFrame
import scala.swing.BorderPanel
import BorderPanel.Position._
import scala.swing.Button
import java.awt.Dimension
import java.awt.Color
import scala.swing.ScrollPane
import scala.swing.FlowPanel
import scala.swing.Component
import scala.collection.mutable.ArrayBuffer
import scala.swing.event.ButtonClicked
import scala.swing.ComboBox
import scala.swing.Slider
import scala.swing.event.ValueChanged
import scala.swing.Label
import scala.swing.ComboBox
import scala.swing.event.SelectionChanged
import scala.swing.event.SelectionChanged
import scala.actors.scheduler.ThreadPoolConfig

object Main extends SimpleSwingApplication {
	
	System.setProperty("actors.maxPoolSize","2000")
//	println(System.getProperty("actors.maxPoolSize"))

	val storedConfiguration = Array(
			Set(Array(24,2),Array(25,2),Array(24,3),Array(26,3),Array(12,4),Array(25,4),Array(26,4),Array(27,4),Array(9,5),
					Array(10,5),Array(11,5),Array(12,5),Array(16,5),Array(17,5),Array(26,5),Array(27,5),Array(28,5),Array(35,5),Array(36,5),
					Array(8,6),Array(9,6),Array(10,6),Array(11,6),Array(15,6),Array(16,6),Array(25,6),Array(26,6),Array(27,6),Array(35,6),
					Array(36,6),Array(1,7),Array(2,7),Array(8,7),Array(11,7),Array(15,7),Array(16,7),Array(18,7),Array(20,7),Array(21,7),
					Array(24,7),Array(26,7),Array(1,8),Array(2,8),Array(8,8),Array(9,8),Array(10,8),Array(11,8),Array(16,8),Array(18,8),
					Array(20,8),Array(21,8),Array(24,8),Array(25,8),Array(9,9),Array(10,9),Array(11,9),Array(12,9),Array(16,9),Array(18,9),
					Array(19,9),Array(12,10)),
			Set(Array(28,6),Array(29,6),Array(39,6),Array(40,6),Array(27,7),Array(29,7),Array(39,7),Array(40,7),Array(5,8),Array(6,8),
					Array(14,8),Array(15,8),Array(27,8),Array(28,8),Array(5,9),Array(6,9),Array(13,9),Array(15,9),Array(13,10),Array(14,10),
					Array(21,10),Array(22,10),Array(21,11),Array(23,11),Array(21,12),Array(40,13),Array(41,13),Array(40,14),Array(42,14),
					Array(40,15),Array(29,18),Array(30,18),Array(31,18),Array(29,19),Array(30,20)),
			Set(Array(5,7),Array(6,7),Array(7,7),Array(8,7),Array(9,7),Array(10,7),Array(11,7),Array(12,7),Array(13,7),Array(14,7)),
			Set(Array(8,6),Array(9,6),Array(7,7),Array(8,7),Array(8,8)),
			Set[Array[Int]]()
	)
	val confComboBox = new ComboBox(Seq("Gosper Glider Gun", "Ships", "Ten", "Pentonimo", "Empty"))
	
	var _speedCoeff = 1.0
	val sleepTime = 1.0//secondi
	

	var grid = new CellGrid(30, 45)

	var pane = new ScrollPane(grid)
	
	def top = new MainFrame {
			
		contents = new BorderPanel {
			
			resizable = false
			layout(pane) = Center;
			
			val controlPanel = new FlowPanel {
				val saveConf = new Button("Save Cfg")
				val loadConf = new Button("Load")
				val nextButton = new Button("Next")
				val playButton = new Button("Play/Pause")
				val speedSlider = new Slider
				speedSlider.min = 1
				speedSlider.max = 10
				speedSlider.value = 5
				

				listenTo(nextButton)
				listenTo(loadConf)
				listenTo(saveConf)
				listenTo(playButton)
				listenTo(speedSlider)
				
				reactions += {
				case ButtonClicked(`nextButton`) =>{next}
				case ButtonClicked(`loadConf`) =>{runController("Alt");loadConfiguration}
				case ButtonClicked(`saveConf`) =>{runController("Alt");saveConfiguration}
				case ButtonClicked(`playButton`) =>{runController("Play/Pause")}
				case ValueChanged(`speedSlider`) =>{speedCoef = speedSlider.value}
				}
	
				contents += saveConf
				contents += confComboBox
				contents += loadConf
				contents += nextButton
				contents += playButton
				contents += speedSlider
			}
			
			layout(controlPanel) = South;
			grid.contents.foreach(_ match {
			case value:MyCell => new Thread(value).start()
			})
			
		}
		
	}
	
	
	def runController(message:String) = {
		grid.contents.foreach(_ match {
		case value:MyCell => value.executing = message;
		})
	}
	
	def next() = {
		grid.next()
//		var total = false;
//		previousWorld.foreach(_.foreach(total ||= _))
//		if (total == false) AutomaticEsecutor ! "a"
	}
	
	def loadConfiguration() = {
				
		val selectedConf = storedConfiguration(confComboBox.selection.index)
		
		grid.cells.foreach(elem => 
			if ({var found = false;
					selectedConf.foreach(item => 
						item match{
							case Array(elem.x,elem.y)=> found = true;
							case _ =>
							}
						);
					found})
				elem.living = true
			else
				elem.living = false
			)
	}
	
	def saveConfiguration() = {
//		grid.cells.foreach(i => if (i.isAlive()) print("Array(" + i.x + ","+ i.y + ")" + ","))
//		println
	}

	def speedCoef_= (value:Int) = _speedCoeff = value
	def speedCoef = _speedCoeff
}