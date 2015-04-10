package tuneup.services

class TuneupSeries {}

object TuneupSeries {

  trait SeriesJson {
    def toJson: String
  }

  case class SeriesList(list: List[SeriesData]) extends SeriesJson {
    def toJson = "[ " + list.map(x => x.toJson).mkString(", ") + " ]"
  }

  case class SeriesData(slave: String, resource: String, value: Double) extends SeriesJson {
    def toJson = "{ \"name\": \"slave." + slave + ".resource." + resource + "\", \"columns\": [ \"value\" ], \"points\": [ [" + value + "] ] }"
  }

}
