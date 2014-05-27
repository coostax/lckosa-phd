;the expert agent class
(import pt.iscte.pramc.tests.lof.scn.expert.HandExpert.java)
;domain imports
(import pt.iscte.pramc.tests.lof.scn.expert.SimHand)
(import pt.iscte.pramc.tests.lof.scn.domain.FingerState)
(import pt.iscte.pramc.tests.lof.scn.domain.Number)


;define templates accordingly to java classes
;definition SimHand
(deftemplate SimHand (declare (from-class SimHand)))
;definition FingerState
(deftemplate FingerState (declare (from-class FingerState)))
;definition number
(deftemplate Number (declare (from-class Number)))

;RULES

(defrule oneIndex
	"on one only the index finger must be up"
	(Number {value == 1})
	(SimHand {index == "DOWN"} (index ?val))
	=>
    (handExpert changeIndex  ?val)
)