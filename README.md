# GENERATOR COMPONENT AEM
<br/>
<p>It is a useful tool, that can generate all the components you need in a short period of time, if you are running a HTL project</p>

[![ VIDEO ](https://img.youtube.com/vi/JmJfeXpgVqU/0.jpg)](https://www.youtube.com/watch?v=JmJfeXpgVqU&t=1s)

## Syntax

```java
<component-name> <extends-from> <dialog-fields>
```

## Available Options

```
<component-name>
```
can ben any name, without space, and no special characters<br/><br/><br/>


```
<extends-from>
```
<table>
<th>NAME</th>
<th>DESCRIPTION</th>

<tr>
<td>default</td>
<td>A default component that not extends from any component</td>
</tr>

<tr>
<td>image</td>
<td>create a component extending from image</td>
</tr>

<tr>
<td>container</td>
<td>create a component extending from container</td>
</tr>
</table><br/><br/>

```
<dialog-fields>
```
<table>
<th>NAME</th>
<th>DESCRIPTION</th>

<tr>
<td>textfield</td>
<td>Simple text field</td>
</tr>

<tr>
<td>select</td>
<td>Select field with one option, you have to add more</td>
</tr>

<tr>
<td>richtext</td>
<td>Text area with rich text tools enabled</td>
</tr>

<tr>
<td>checkbox</td>
<td>Simple check box alter as you need</td>
</tr>
</table>

## Config Options
<p>To create the files, the software need to know where is located your project folder, you have to specify where is the core java components and the components into ui.apps, use the config.cfg file inside config folder

### config.cfg
```
PACKAGE_BASE=<java-package>
PATH_JAVA=<core-java-model-path>
PATH_COMPONENTS=<ui.apps-components-path>
```
<br/>
<p>Into group properties file, you need to specify all the groups you work inside your project</p>

### group.cfg
```
1=Project Enablement - Content
2=Project Enablement - Item
3=Add more as you need it...
```
## How to integrate with my project
<p>You just need to copy the files inside release folder here, the jar file and the config folder, copy and paste into your project root path folder</p>

<p>After copying, open the config folder and open the config.cfg, specify the needed folders. in the right properties, after that configure the group.cfg with your project groups, then you are ready to use..</p>

<h3>Open the Terminal</h3>
Type the code below, to create a simple component as an example.

```java
java -jar aemgc.jar card default "[{textfield:title},{select:option},{richtext:content},{checkbox:background}]"
```

