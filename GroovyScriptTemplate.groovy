#!/usr/bin/env groovy

/* 
 * This Groovy Script Template demonstrates several core concepts of Groovy and Groovy-Scripting.
 * If executed, the script performs several harmless read-only operations, which print to the console.
 * 
 * License: MIT
 */

 
// INIT =======================================================================

// download and import some dependencies from Maven Central Repo
// SLF4J is for logging:
@Grab(group='org.slf4j', module='slf4j-simple', version='1.7.16')

// Fluent API of Apache HTTP Client is for REST calls
@Grab(group='org.apache.httpcomponents', module='fluent-hc', version='4.5.6')

// Import used classes/packages
import org.slf4j.impl.SimpleLogger
import org.apache.http.client.fluent.Request

// set up logging (write to console for now)
System.properties[SimpleLogger.LOG_FILE_KEY] = 'System.out'
System.properties[SimpleLogger.DEFAULT_LOG_LEVEL_KEY ] = 'debug'  // trace|debug|info|warn|error|off
System.properties[SimpleLogger.SHOW_THREAD_NAME_KEY] = 'false'
log = new SimpleLogger('')



// MAIN BLOCK =================================================================
// demonstrating several common concepts

step('SimpleMethod')
mySimpleMethod('bar')

step('Strings and GStrings')
myGstringsTest() 

step('Method with Named Parameters')
myMethodWithNamedParams(bar: 42, foo: 'foo')

step('Strong Typing')
myStrongTypingTest('foobar')

step('Conditions')
myConditionsTest()

step('Loops')
myLoopTest()

step('Lists')
myListTest()

step('Maps')
myMapTest()

step('REST Call')
myRestCall()

step('Exception Handling')
myExceptionTest()

step('Method Missing')
giveMeIceCream()

step('END')



// METHODS ====================================================================

// print info about current step (using println and proper logging system)
def step(stepName) {
	println '\n------------------------------------------------------------'
	log.info("Step: ${stepName}")
}

// simple method with one parameter, which uses the logging system
def mySimpleMethod(myparam) {
	log.debug("foo: ${myparam}")
}

// play with Strings and GStrings
def myGstringsTest() {
	// regular string (single quotes)
	bar = 'bar'
	
	// GStrings (double quotes) support interpolation (variable resolution)
	// eager (resolution upon creation of GString):
	eagerGstring = "foo and ${bar}"
	log.debug(eagerGstring)
	
	// lazy (note the closure; resolution upon every coercion of GString to String):
	lazyGstring = "foo and ${ -> bar}"
	log.debug("lazyPre:  " + lazyGstring)
	bar = 'nubar'
	log.debug("lazyPost: " + lazyGstring)
}

// method with named parameters and a return
def myMethodWithNamedParams(Map args) {
	myString = "foo: ${args.foo}, bar: ${args.bar}"
    log.debug(myString)
	return myString
}

// strong typing (and autoboxing) in action
Integer myStrongTypingTest(String input) {
	int len = input.length()
	log.debug("${input} --> ${len} characters")
	return len
}

// play with control structures
def myConditionsTest() {
	// generate random number and coerce into an int
	rnd = Math.random() * 10 as int;
	log.debug("Random: ${rnd}")
	
	// if-else
	if (rnd > 6) {
		log.debug('a_gt6')
	} else if (rnd > 3) {
		log.debug('a_gt3')
	} else {
		log.debug('a_le3')
	}
	
	// ternary operator
	res = (rnd > 4) ? 'b_gt4' : 'b_le4'
	log.debug(res)
	
	// short circuit
	rnd > 5 && log.debug('c_gt5')
	rnd > 5 || log.debug('c_le5')
}

// simple for loops
def myLoopTest() {
	String heyhey = ''
	for (int i=1; i<=3; i++) {
		heyhey += 'hey! '
	}
	log.debug(heyhey)
	
	String haha = '';
	for (i in 0..5 ) {
		haha += 'ha'
    }
	log.debug("${haha}!")
}

// play with a list
def myListTest() {
	// create list (create empty with [])
	myList = ['fooArray1']
	
	// append to list
	myList << 'fooArray2'
	
	// loop over list
	myList.each { item ->
		log.debug("${item}")
	}
}

// play with a map
def myMapTest() {	
	// create map
	myMap = [:]
	
	// add to map
	myMap << [fooKey1:'barValue1']
	myMap.fooKey2 = 'barValue2'

	// read from map
	log.debug(myMap['fooKey1'])
	log.debug(myMap.fooKey2)
}

// play with exceptions
def myExceptionTest() {
	try {
		// this should crash
		'NaN'.toInteger()
	} catch (e) {
		// catch-and-log is a bad practice, but that's beyond the scope of this template
		log.warn(e.toString())
	}

}

// get data from remote REST API
def myRestCall() {
	responseString = Request.Get('https://reqres.in/api/unknown/2').execute().returnContent().asString();
	log.debug(responseString)
}

// when trying to call an undefined method, the below method will handle this (metaprogramming; see also: propertyMissing)
def methodMissing(String name, args) {
	if (name.startsWith('giveMe')) {
		what = name.replace('giveMe', '')
		log.info("here is your ${what}")
	} else {
		throw new MissingMethodException(name, this.class, args)
	}
}
